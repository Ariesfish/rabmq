package xyz.ariesfish.work;

import com.rabbitmq.client.*;
import xyz.ariesfish.utils.ConnectionUtil;

import java.io.IOException;

public class ConsumerX {
    private static final String QUEUE_NAME = "test_work_queue";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        channel.basicQos(1);

        // 事件模型
        // 定义消费者
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
        // 监听队列
        boolean autoAck = false; // 自动应答关闭
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }
}
