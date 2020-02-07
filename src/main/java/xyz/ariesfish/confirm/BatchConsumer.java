package xyz.ariesfish.confirm;

import com.rabbitmq.client.*;
import xyz.ariesfish.utils.ConnectionUtil;

import java.io.IOException;

public class BatchConsumer {
    private static final String QUEUE_NAME = "test_queue_confirm_batch";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 事件模型
        // 定义消费者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "utf-8");
                System.out.println("Receive Message: " + msg);
            }
        };
        // 监听队列
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
