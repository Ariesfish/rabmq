package xyz.ariesfish.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import xyz.ariesfish.utils.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {

    private static final String QUEUE_NAME = "test_simple_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectionUtil.getConnection();
        // 获取一个通道
        Channel channel = connection.createChannel();
        // 创建队列声明
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        String msg = "hello simple queue!!";

        channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
        System.out.println("Send Message: " + msg);

        channel.close();
        connection.close();
    }
}
