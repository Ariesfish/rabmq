package xyz.ariesfish.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import xyz.ariesfish.utils.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {
    private static final String QUEUE_NAME = "test_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // 获取连接
        Connection connection = ConnectionUtil.getConnection();
        // 获取一个通道
        Channel channel = connection.createChannel();
        // 创建队列声明
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);

        // 从消费者受到确认反馈前只发一个消息
        channel.basicQos(1);

        for (int i = 1; i <= 50; i++) {
            String msg = "hello work queue: " + i;

            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            System.out.println("Send Message: " + msg);

            Thread.sleep(20);
        }

        channel.close();
        connection.close();
    }
}
