package com.lxc.code.mqrabbitmq.constants;

public interface RabbitConsts {
    public static final String TEST_QUEUE = "lxc-test_queue";


    /**************订阅*********/
    /*交换机*/
    public static final String TEST_EXCHANGE = "fanout_amqp_exchange";


    /*队列1*/
    public static final String TEST_QUEUE_1 = "fanout_amqp_queue_1";
    /*队列2*/
    public static final String TEST_QUEUE_2 = "fanout_amqp_queue_2";


    /**************路由*********/
    public static final String DIRECT_TEST_EXCHANGE = "direct_amqp_exchange";
    /*队列1*/
    public static final String DIRECT_TEST_QUEUE_1 = "direct_amqp_queue_1";
    /*队列2*/
    public static final String DIRECT_TEST_QUEUE_2 = "direct_amqp_queue_2";


    /**************重试 *********/

    public static final String RETRY_TEST_EXCHANGE = "retry_amqp_exchange";

    public static final String RETRY_TEST_QUEEN = "retry_test_queen";



    public static final String RETRY_TEST_EXCHANGE_10 = "retry_amqp_exchange_10";

    public static final String RETRY_TEST_QUEEN_10 = "retry_test_queen_10";

    public static final String RETRY_TEST_QUEEN_11 = "retry_test_queen_11";
}
