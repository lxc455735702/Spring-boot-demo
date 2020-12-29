package com.lxc.code.mqrabbitmq.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lxc.code.mqrabbitmq.config.RabbitMqConfig;
import com.lxc.code.mqrabbitmq.constants.RabbitConsts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Component
@Slf4j
public class RabbitService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

        private final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            if (ack) {
                //成功业务逻辑
                log.info("消息投递到及交换机成功啦！！！");
            } else {
                //失败业务逻辑
                log.info("消息投递到及交换机失败啦！！");
            }
        }
    };

    // 如果消息从交换器发送到对应队列失败时触发
    private final RabbitTemplate.ReturnsCallback returnCallback =
            new RabbitTemplate.ReturnsCallback() {

                @Override
                public void returnedMessage(ReturnedMessage returnedMessage) {
                    log.info("message=" + returnedMessage.getMessage().toString());
                    log.info("replyCode=" + returnedMessage.getReplyCode());
                    log.info("replyText=" + returnedMessage.getReplyText());
                    log.info("exchange=" + returnedMessage.getExchange());
                    log.info("routingKey=" + returnedMessage.getRoutingKey());

                }
            };

    /*发送消息到队列*/
    public String sendQueue(Object payload) {
        return baseSend("", RabbitConsts.TEST_QUEUE, payload, null, null);
    }

    /*发送到交换器*/
    public String sendExchange(Object payload, String routingKey) {
        return baseSend(RabbitConsts.TEST_EXCHANGE, routingKey, payload, null, null);
    }

    public String sendDirectExchange(Object payload, String routingKey) {
        return baseSend(RabbitConsts.DIRECT_TEST_EXCHANGE, routingKey, payload, null, null);
    }


    /**
     * MQ 公用发送方法
     *
     * @param exchange              交换机
     * @param routingKey            队列
     * @param payload               消息体
     * @param messageId             消息id（唯一性）
     * @param messageExpirationTime 持久化时间
     * @return 消息编号
     */
    public String baseSend(String exchange, String routingKey, Object payload, String messageId, Long messageExpirationTime) {
        /*若为空，则自动生成*/
        if (messageId == null) {
            messageId = UUID.randomUUID().toString();
        }
        String finalMessageId = messageId;
        /*设置消息属性*/
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                /*消息属性中写入消息id*/
                message.getMessageProperties().setMessageId(finalMessageId);
                /*设置消息持久化时间*/
                if (!StringUtils.isEmpty(messageExpirationTime)) {
                    message.getMessageProperties().setExpiration(messageExpirationTime.toString());
                }
                /*设置消息持久化*/
                message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                return message;
            }
        };

        /*构造消息体，转换json数据格式*/
        Message message = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(payload);
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentEncoding(MessageProperties.CONTENT_TYPE_JSON);
            message = new Message(json.getBytes(), messageProperties);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        /*表示当前消息唯一性*/
        CorrelationData correlationData = new CorrelationData(finalMessageId);
        rabbitTemplate.setConfirmCallback(this.confirmCallback);
        rabbitTemplate.setReturnsCallback(this.returnCallback);
        /**
         * public void convertAndSend(String exchange, String routingKey, Object message,
         * MessagePostProcessor messagePostProcessor, @Nullable CorrelationData correlationData) throws AmqpException
         * exchange: 路由
         * routingKey: 绑定key
         * message: 消息体
         * messagePostProcessor: 消息属性处理器
         * correlationData: 表示当前消息唯一性
         */
        rabbitTemplate.convertAndSend(exchange, routingKey, message, messagePostProcessor, correlationData);

        return finalMessageId;
    }

}
