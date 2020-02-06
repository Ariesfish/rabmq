package xyz.ariesfish.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionUtil {

    public static Connection getConnection() throws IOException, TimeoutException {
        // 定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();

        // 设置服务地址
        factory.setHost("127.0.0.1");

        // 设置AMQP端口: 5672
        factory.setPort(5672);

        // 设置vhost
        factory.setVirtualHost("/vhost_rmq");

        // 设置用户名，密码
        factory.setUsername("maurice");
        factory.setPassword("suzhou123");

        return factory.newConnection();
    }
}
