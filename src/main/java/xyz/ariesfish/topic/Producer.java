package xyz.ariesfish.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import xyz.ariesfish.utils.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {
    private static final String EXCHANGE_NAME = "test_exchange_topic";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // 获取连接
        Connection connection = ConnectionUtil.getConnection();
        // 获取一个通道
        Channel channel = connection.createChannel();
        // 声明转发器
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        String msg = "hello topic mode!!";

        channel.basicPublish(EXCHANGE_NAME, "goods.delete", null, msg.getBytes());
        System.out.println("Send Message: " + msg);

        channel.close();
        connection.close();
    }
}
