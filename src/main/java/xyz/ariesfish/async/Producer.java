package xyz.ariesfish.async;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import xyz.ariesfish.utils.ConnectionUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

public class Producer {
    private static final String QUEUE_NAME = "test_queue_confirm_async";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        channel.confirmSelect();

        // 存放未确认的消息标识
        final SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());

        // 添加ConfirmListener
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                if (multiple) {
                    System.out.println("Handle Ack Multiple");
                    confirmSet.headSet(deliveryTag+1).clear(); // 删除多个标识
                } else {
                    System.out.println("Handle Ack Not Multiple");
                    confirmSet.remove(deliveryTag); // 删除一个标识
                }
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                if (multiple) {
                    System.out.println("Handle Nack Multiple");
                    confirmSet.headSet(deliveryTag+1).clear();
                } else {
                    System.out.println("Handle Nack Not Multiple");
                    confirmSet.remove(deliveryTag);
                }
            }
        });

        String msg = "Hello Async Confirm";

        while (true) {
            long seqNo = channel.getNextPublishSeqNo();
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            confirmSet.add(seqNo); // 加入unconfirm集合
        }
    }
}
