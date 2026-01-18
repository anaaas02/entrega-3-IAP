package producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import util.RabbitMQConfig;

public class GeoJSONProducer {
	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");

		try (Connection connection = factory.newConnection();
				Channel channel = connection.createChannel()) {
			channel.exchangeDeclare(RabbitMQConfig.INPUT_EXCHANGE, "direct", true);

			String[] vehicles = { "1122-XYZ", "3344-JKL" };
			double[][] coords = { { 41.3879, 2.1699 }, { 37.3891, -5.9845 } };

			for (int i = 0; i < vehicles.length; i++) {
				String message = buildGeoJson(vehicles[i], coords[i][0], coords[i][1]);
				channel.basicPublish(RabbitMQConfig.INPUT_EXCHANGE, RabbitMQConfig.ROUTING_KEY_GEOJSON, null,
						message.getBytes("UTF-8"));
				System.out.println("Sent GeoJSON: " + message);
				Thread.sleep(500);
			}
		}
	}

	private static String buildGeoJson(String vehicleId, double latitude, double longitude) {
		return "{"
				+ "\"type\":\"Feature\","
				+ "\"geometry\":{\"type\":\"Point\",\"coordinates\":[" + latitude + "," + longitude + "]},"
				+ "\"properties\":{\"vehicle\":\"" + vehicleId + "\"}"
				+ "}";
	}
}
