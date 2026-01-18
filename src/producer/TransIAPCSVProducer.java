package producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import util.RabbitMQConfig;

public class TransIAPCSVProducer {
	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");

		try (Connection connection = factory.newConnection();
				Channel channel = connection.createChannel()) {
			channel.exchangeDeclare(RabbitMQConfig.INPUT_EXCHANGE, "direct", true);

			String[] messages = {
					"5566-QWE,39.4699,-0.3763",
					"9900-RTY,43.2630,-2.9350"
			};

			for (String message : messages) {
				channel.basicPublish(RabbitMQConfig.INPUT_EXCHANGE, RabbitMQConfig.ROUTING_KEY_CSV, null,
						message.getBytes("UTF-8"));
				System.out.println("Sent CSV: " + message);
				Thread.sleep(500);
			}
		}
	}
}
