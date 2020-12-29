package com.lxc.code.mqrabbitmq.config;


import com.lxc.code.mqrabbitmq.constants.RabbitConsts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitMqConfig {

    @Bean(RabbitConsts.TEST_QUEUE)
    public Queue testQueue() {
        return new Queue(RabbitConsts.TEST_QUEUE, true);
    }

    /*声明一个fanout交换机*/
    @Bean(RabbitConsts.TEST_EXCHANGE)
    public Exchange testExchange() {
        // durable(true)持久化，mq重启之后，交换机还在
        return ExchangeBuilder.fanoutExchange(RabbitConsts.TEST_EXCHANGE).durable(true).build();
    }

    /**
     * 声明队列1
     * public Queue(String name, boolean durable, boolean exclusive, boolean autoDelete) {
     * this(name, durable, exclusive, autoDelete, (Map)null);
     * }
     * String name: 队列名
     * boolean durable: 持久化消息队列，rabbitmq 重启的时候不需要创建新的队列，默认为 true
     * boolean exclusive: 表示该消息队列是否只在当前的connection生效，默认为 false
     * boolean autoDelete: 表示消息队列在没有使用时将自动被删除，默认为 false
     */
    @Bean(RabbitConsts.TEST_QUEUE_1)
    public Queue testQueue1() {
        return new Queue(RabbitConsts.TEST_QUEUE_1, true);
    }

    /*声明队列2*/
    @Bean(RabbitConsts.TEST_QUEUE_2)
    public Queue testQueue2() {
        return new Queue(RabbitConsts.TEST_QUEUE_2, true);
    }


    /*队列1与路由进行绑定*/
    @Bean
    Binding bindingTest1(@Qualifier(RabbitConsts.TEST_QUEUE_1) Queue queue,
                         @Qualifier(RabbitConsts.TEST_EXCHANGE) Exchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("")
                .noargs();
    }

    /*队列2与路由进行绑定*/
    @Bean
    Binding bindingTest2(@Qualifier(RabbitConsts.TEST_QUEUE_2) Queue queue,
                         @Qualifier(RabbitConsts.TEST_EXCHANGE) Exchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("")
                .noargs();
    }

    /***************************** 路由模式  *************/


    @Bean(RabbitConsts.DIRECT_TEST_QUEUE_1)
    public Queue DTestQueue1() {
        return new Queue(RabbitConsts.DIRECT_TEST_QUEUE_1, true);
    }

    /*声明队列2*/
    @Bean(RabbitConsts.DIRECT_TEST_QUEUE_2)
    public Queue DTestQueue2() {
        return new Queue(RabbitConsts.DIRECT_TEST_QUEUE_2, true);
    }


    /*声明一个fanout交换机*/
    @Bean(RabbitConsts.DIRECT_TEST_EXCHANGE)
    public Exchange DTestExchange() {
        // durable(true)持久化，mq重启之后，交换机还在
        return ExchangeBuilder.directExchange(RabbitConsts.DIRECT_TEST_EXCHANGE).durable(true).build();
    }

    /*队列1与路由进行绑定*/
    @Bean
    Binding bindingDirectTest1(@Qualifier(RabbitConsts.DIRECT_TEST_QUEUE_1) Queue queue,
                               @Qualifier(RabbitConsts.DIRECT_TEST_EXCHANGE) Exchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("SF")
                .noargs();
    }

    /*队列2与路由进行绑定*/
    @Bean
    Binding bindingDirectTest2(@Qualifier(RabbitConsts.DIRECT_TEST_QUEUE_2) Queue queue,
                               @Qualifier(RabbitConsts.DIRECT_TEST_EXCHANGE) Exchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("JD")
                .noargs();
    }


    /**************************  重试  **********************/


    @Bean(RabbitConsts.RETRY_TEST_QUEEN)
    public Queue retestQueue() {
        return new Queue(RabbitConsts.RETRY_TEST_QUEEN, true);
    }

    @Bean(RabbitConsts.RETRY_TEST_EXCHANGE)
    public Exchange RetryTestExchange() {
        // durable(true)持久化，mq重启之后，交换机还在
        return ExchangeBuilder.directExchange(RabbitConsts.RETRY_TEST_EXCHANGE).durable(true).build();
    }

    @Bean
    Binding RetryDirectTest(@Qualifier(RabbitConsts.RETRY_TEST_QUEEN) Queue queue,
                            @Qualifier(RabbitConsts.RETRY_TEST_EXCHANGE) Exchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("retry")
                .noargs();
    }

    @Bean(RabbitConsts.RETRY_TEST_QUEEN_10)
    public Queue retestQueue10() {
        return new Queue(RabbitConsts.RETRY_TEST_QUEEN_10, true);
    }

    @Bean(RabbitConsts.RETRY_TEST_QUEEN_11)
    public Queue retestQueue11() {
        return new Queue(RabbitConsts.RETRY_TEST_QUEEN_11, true);
    }

    @Bean(RabbitConsts.RETRY_TEST_EXCHANGE_10)
    public Exchange RetryTestExchange10() {
        // durable(true)持久化，mq重启之后，交换机还在
        return ExchangeBuilder.directExchange(RabbitConsts.RETRY_TEST_EXCHANGE_10).durable(true).build();
    }

    @Bean
    Binding RetryDirectTest10(@Qualifier(RabbitConsts.RETRY_TEST_QUEEN_10) Queue queue,
                            @Qualifier(RabbitConsts.RETRY_TEST_EXCHANGE_10) Exchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("111")
                .noargs();
    }

    @Bean
    Binding RetryDirectTest11(@Qualifier(RabbitConsts.RETRY_TEST_QUEEN_11) Queue queue,
                              @Qualifier(RabbitConsts.RETRY_TEST_EXCHANGE_10) Exchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("222")
                .noargs();
    }


    @Bean
    public MessageRecoverer messageRecoverer(RabbitTemplate rabbitTemplate) {
        return new RepublishMessageRecoverer(rabbitTemplate, RabbitConsts.RETRY_TEST_EXCHANGE, "retry");
    }

}
