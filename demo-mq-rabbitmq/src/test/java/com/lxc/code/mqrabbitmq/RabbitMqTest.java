package com.lxc.code.mqrabbitmq;

import com.lxc.code.mqrabbitmq.service.RabbitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootTest
public class RabbitMqTest {
    @Autowired
    private RabbitService rabbitMqService;


    @Test
    public void test() {
        String str = "这是一条测试数据";
        rabbitMqService.sendQueue(str);
    }



    @Test
    public void test2(){
        for (int i = 0;i < 10; i++){
            String s = "消息" + i;
            rabbitMqService.sendQueue(s);
        }
    }


    @Test
    public void test3(){
        String s = "广播快递";
        rabbitMqService.sendExchange(s,"");
    }


    @Test
    public void test4(){
        String s = "京东快递";
        String s1 = "顺丰快递";
        rabbitMqService.sendDirectExchange(s,"JD");
        rabbitMqService.sendDirectExchange(s1,"SF");
    }


    @Test
    public void sendMsg(){
        rabbitMqService.sendDirectExchange("测试消息","xxx");
    }
}
