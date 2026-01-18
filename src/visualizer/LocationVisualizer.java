package visualizer;

import java.nio.charset.StandardCharsets;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import util.RabbitMQConfig;

public class LocationVisualizer {
	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");

		try (Connection connection = factory.newConnection();
				Channel channel = connection.createChannel()) {
			channel.exchangeDeclare(RabbitMQConfig.OUTPUT_EXCHANGE, "fanout", true);
			String queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, RabbitMQConfig.OUTPUT_EXCHANGE, "");

			System.out.println("LocationVisualizer started. Waiting for unified JSON messages...");

			DeliverCallback callback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
				System.out.println("Received: " + message);
			};
			channel.basicConsume(queueName, true, callback, consumerTag -> {
			});

			// Keep the application running.
			synchronized (LocationVisualizer.class) {
				LocationVisualizer.class.wait();
			}
		}
	}
}
