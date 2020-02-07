package xyz.ariesfish.topic;

import com.rabbitmq.client.*;
import xyz.ariesfish.utils.ConnectionUtil;

import java.io.IOException;

public class ConsumerX {

    private static final String QUEUE_NAME = "test_queue_topic_1";
    private static final String EXCHANGE_NAME = "test_exchange_topic";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 绑定队列到转发器
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "goods.add");
        channel.basicQos(1);

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "utf-8");
                System.out.println("[X] Receive Message: " + msg);

                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("[X] Done!");
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };

        channel.basicConsume(QUEUE_NAME, false, consumer);
    }
}
