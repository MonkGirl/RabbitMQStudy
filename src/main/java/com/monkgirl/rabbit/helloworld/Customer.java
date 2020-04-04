package com.monkgirl.rabbit.helloworld;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author MissYoung
 * @version 0.1
 * @description 消费者
 * @date 2019-11-28 15:56
 */
public class Customer {
    private final static String QUEUE_NAME = "hello";

    public static void main(String...args){
        // 创建连接工厂
        ConnectionFactory factory= new ConnectionFactory();
        // 设置RabbitMQ地址
        factory.setHost("127.0.0.1");
        try(Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            // 声明要关注的队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println("C [*] Waiting for message, To exit press CTRL+C");

            Consumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    super.handleDelivery(consumerTag, envelope, properties, body);
                    String msg = new String(body, StandardCharsets.UTF_8);
                    System.out.println("C [*] Received '" + msg + "'");
                }
            };

            // 自动回复队列应答
            channel.basicConsume(QUEUE_NAME, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
