package com.lxc.code.mqrabbitmq.service;

import com.lxc.code.mqrabbitmq.config.RabbitMqConfig;
import com.lxc.code.mqrabbitmq.constants.RabbitConsts;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RecService {
    /*队列*/

    @RabbitListener(queues = RabbitConsts.TEST_QUEUE)
    public void receiveMsg(Message message, Channel channel) {
        String msg = new String(message.getBody());
        if (msg == null) {
            System.out.println("消息为空");
        }
        System.out.println("收到消息内容：" + msg);
    }

    @RabbitListener(queues = RabbitConsts.TEST_QUEUE_1)
    public void receiveMsg1(Message message, Channel channel) {
        try {
            String msg = new String(message.getBody());
            if (msg == null) {
                System.out.println("消息为空");
            }
            System.out.println("收到消息内容 TEST_QUEUE_1：" + msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = RabbitConsts.TEST_QUEUE_2)
    public void receiveMsg2(Message message, Channel channel) {
        try {
            String msg = new String(message.getBody());
            if (msg == null) {
                System.out.println("消息为空");
            }
            System.out.println("收到消息内容 TEST_QUEUE_2：" + msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    @RabbitListener(queues = RabbitConsts.DIRECT_TEST_QUEUE_1)
//    public void receiveMsg3(Message message, Channel channel) throws IOException {
//        String msg = new String(message.getBody());
//        if (msg == null) {
//            System.out.println("消息为空");
//        }
//        System.out.println("路由模式 收到消息内容 TEST_QUEUE_1：" + msg);
//        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
//    }


//    @RabbitListener(queues = RabbitConsts.DIRECT_TEST_QUEUE_2)
//    public void receiveMsg4(Message message, Channel channel) {
//        String msg = new String(message.getBody());
//        if (msg == null) {
//            System.out.println("消息为空");
//        }
//        System.out.println("路由模式 收到消息内容 TEST_QUEUE_2：" + msg);
//        int i = 1 / 0;
//    }


    @RabbitListener(queues = RabbitConsts.RETRY_TEST_QUEEN)
    public void t3(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody());
        System.out.println("重试队列  RETRY_TEST_QUEEN 收到了" + msg);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        channel.basicAck(deliveryTag, false);
    }


    @RabbitListener(queues = RabbitConsts.RETRY_TEST_QUEEN_10)
    public void t10(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody());
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        System.out.println("重试消费者10 收到了=-=" + msg);
//        if(msg.contains("测试")){
//            throw new RuntimeException();
//        }
        channel.basicAck(deliveryTag,false);
    }

    @RabbitListener(queues = RabbitConsts.RETRY_TEST_QUEEN_11)
    public void t11(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody());
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        System.out.println("重试消费者11 收到了=-=" + msg);
        if(msg.contains("测试")){
            throw new RuntimeException();
        }
        channel.basicAck(deliveryTag,false);
    }
}
