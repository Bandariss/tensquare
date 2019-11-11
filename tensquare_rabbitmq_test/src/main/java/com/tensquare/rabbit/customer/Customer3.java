package com.tensquare.rabbit.customer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@RabbitListener(queues = "douyu")
@Component
public class Customer3 {
    @RabbitHandler
    public void getMsg(String msg){
        System.out.println("douyu:"+msg);
    }
}
