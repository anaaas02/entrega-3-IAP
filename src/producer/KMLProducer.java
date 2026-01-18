package producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import util.RabbitMQConfig;

public class KMLProducer {
	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");

		try (Connection connection = factory.newConnection();
				Channel channel = connection.createChannel()) {
			channel.exchangeDeclare(RabbitMQConfig.INPUT_EXCHANGE, "direct", true);

			String[] vehicles = { "0245-MQL", "7788-ABC" };
			double[][] coords = { { 38.3456, -0.4839 }, { 40.4168, -3.7038 } };

			for (int i = 0; i < vehicles.length; i++) {
				String message = buildKml(vehicles[i], coords[i][0], coords[i][1]);
				channel.basicPublish(RabbitMQConfig.INPUT_EXCHANGE, RabbitMQConfig.ROUTING_KEY_KML, null,
						message.getBytes("UTF-8"));
				System.out.println("Sent KML: " + message);
				Thread.sleep(500);
			}
		}
	}

	private static String buildKml(String vehicleId, double latitude, double longitude) {
		return "<kml><Placemark><Point><coordinates>" + latitude + "," + longitude
				+ "</coordinates></Point><Vehicle id=\"" + vehicleId + "\"/></Placemark></kml>";
	}
}
