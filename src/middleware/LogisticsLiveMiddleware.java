package middleware;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import db.DatabaseLogger;
import model.LocationUpdate;
import util.JsonFormatter;
import util.MessageParsers;
import util.RabbitMQConfig;
import util.SntnClient;

public class LogisticsLiveMiddleware {
	private static final String SNTN_BASE_URL = "http://localhost:8080/";

	private final SntnClient sntnClient;
	private final DatabaseLogger databaseLogger;
	private final Connection connection;
	private final Channel outputChannel;

	public LogisticsLiveMiddleware(Connection connection) throws IOException {
		this.connection = connection;
		this.sntnClient = new SntnClient(SNTN_BASE_URL);
		this.databaseLogger = new DatabaseLogger("localhost", "3306", "root", "", "stc");
		this.outputChannel = connection.createChannel();
		this.outputChannel.exchangeDeclare(RabbitMQConfig.OUTPUT_EXCHANGE, "fanout", true);
	}

	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();

		LogisticsLiveMiddleware middleware = new LogisticsLiveMiddleware(connection);
		middleware.startConsumers();
		System.out.println("LogisticsLiveMiddleware started. Waiting for messages...");
	}

	private void startConsumers() throws IOException {
		Channel kmlChannel = connection.createChannel();
		Channel geoJsonChannel = connection.createChannel();
		Channel csvChannel = connection.createChannel();

		declareInput(kmlChannel, RabbitMQConfig.QUEUE_KML, RabbitMQConfig.ROUTING_KEY_KML);
		declareInput(geoJsonChannel, RabbitMQConfig.QUEUE_GEOJSON, RabbitMQConfig.ROUTING_KEY_GEOJSON);
		declareInput(csvChannel, RabbitMQConfig.QUEUE_CSV, RabbitMQConfig.ROUTING_KEY_CSV);

		startConsumer(kmlChannel, RabbitMQConfig.QUEUE_KML, Format.KML);
		startConsumer(geoJsonChannel, RabbitMQConfig.QUEUE_GEOJSON, Format.GEOJSON);
		startConsumer(csvChannel, RabbitMQConfig.QUEUE_CSV, Format.CSV);
	}

	private void declareInput(Channel channel, String queue, String routingKey) throws IOException {
		channel.exchangeDeclare(RabbitMQConfig.INPUT_EXCHANGE, "direct", true);
		channel.queueDeclare(queue, true, false, false, null);
		channel.queueBind(queue, RabbitMQConfig.INPUT_EXCHANGE, routingKey);
	}

	private void startConsumer(Channel channel, String queue, Format format) throws IOException {
		DeliverCallback callback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
			System.out.println("Received " + format + " message: " + message);
			try {
				LocationUpdate update = parseMessage(format, message);
				String appKey = sntnClient.fetchAppKey(update.getVehicleId());
				String timestamp = sntnClient.fetchTimestamp();
				String unifiedJson = JsonFormatter.formatUnified(update, appKey, timestamp);
				publishUnified(unifiedJson);
				databaseLogger.logLocation(update);
				System.out.println("Published unified JSON: " + unifiedJson);
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			} catch (Exception ex) {
				System.out.println("Error processing message: " + ex.getMessage());
				channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
			}
		};

		channel.basicConsume(queue, false, callback, consumerTag -> {
		});
	}

	private void publishUnified(String unifiedJson) throws IOException {
		outputChannel.basicPublish(RabbitMQConfig.OUTPUT_EXCHANGE, "", null,
				unifiedJson.getBytes(StandardCharsets.UTF_8));
	}

	private LocationUpdate parseMessage(Format format, String message) {
		switch (format) {
		case KML:
			return MessageParsers.parseKml(message);
		case GEOJSON:
			return MessageParsers.parseGeoJson(message);
		case CSV:
			return MessageParsers.parseCsv(message);
		default:
			throw new IllegalArgumentException("Unsupported format: " + format);
		}
	}

	private enum Format {
		KML,
		GEOJSON,
		CSV
	}
}
