package com.lxc.code.mqrabbitmq.controller;

import com.lxc.code.mqrabbitmq.constants.RabbitConsts;
import com.lxc.code.mqrabbitmq.service.RabbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private RabbitService sender;

    @GetMapping("/test1")
    public String sendMsg(String msg){
        sender.sendExchange(msg,"xxx");
        return "ok";
    }

    @GetMapping("/test2")
    public String sendMsg2(String msg){
        sender.baseSend("xxx","123",null,null,null);
        return "ok";
    }


    @GetMapping("/test3")
    public String sendMsg3(){
        sender.sendDirectExchange("测试消息","SF");
        return "ok";
    }


    @GetMapping("/test4")
    public String sendMsg4(){
        sender.baseSend(RabbitConsts.RETRY_TEST_EXCHANGE,"retry","测试数据",null,null);
        return "ok";
    }


    @GetMapping("/test5")
    public String sendMsg5(){
        sender.baseSend(RabbitConsts.RETRY_TEST_EXCHANGE_10,"111","测试重试数据！！！",null,null);
        return "ok";
    }
}
