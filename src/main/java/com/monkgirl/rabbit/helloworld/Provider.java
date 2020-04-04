package com.monkgirl.rabbit.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author MissYoung
 * @version 0.1
 * @description 生产者
 * @date 2019-11-28 15:57
 */
public class Provider {

    private static final String QUEUE_NAME = "hello";

    public static void main(String... args) {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置RabbitMQ地址
        factory.setHost("127.0.0.1");
        // 创建一个连接
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {

            // 创建一个频道
            // 声明一个队列 队列声明是幂等性的（一个幂等操作的特点是其任意多次执行所产生的影响均与一次
            // 执行的影响相同），也就是说，如果不存在，就创建，如果存在，不会对已存在的队列产生任何影响。
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "Hello World!";
            // 发送消息队列中
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("P [X] Sent '" + message + "'");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }
}
