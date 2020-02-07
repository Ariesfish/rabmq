package xyz.ariesfish.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import xyz.ariesfish.utils.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {
    private static final String EXCHANGE_NAME = "test_exchange_direct";
    private static final String ROUTING_KEY = "info";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // 获取连接
        Connection connection = ConnectionUtil.getConnection();
        // 获取一个通道
        Channel channel = connection.createChannel();
        // 声明转发器
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        String msg = "hello routing mode!!";

        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, msg.getBytes());
        System.out.println("Send Message: " + msg);

        channel.close();
        connection.close();
    }
}
