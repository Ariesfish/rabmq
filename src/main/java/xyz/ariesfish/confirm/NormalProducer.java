package xyz.ariesfish.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import xyz.ariesfish.utils.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class NormalProducer {

    private static final String QUEUE_NAME = "test_queue_confirm_normal";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // 获取连接
        Connection connection = ConnectionUtil.getConnection();
        // 获取一个通道
        Channel channel = connection.createChannel();
        // 创建队列声明
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 生产者调用confirmSelect将channel设置为confirm模式
        channel.confirmSelect();

        String msg = "hello confirm message!!";

        channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
        System.out.println("Send Message: " + msg);

        if (!channel.waitForConfirms()) {
            System.out.println("Send message failed.");
        } else {
            System.out.println("Send message OK.");
        }

        channel.close();
        connection.close();
    }
}
