package com.vergilyn.examples.service;

import java.time.LocalTime;

public class HelloApi {

    private final HelloService helloService;

    public HelloApi() {
        this.helloService = new HelloServiceImpl();
    }

    public String hello(String name){
        return helloService.hello(name);
    }

    public String hello(String name, String message){
        return helloService.hello(name, message);
    }

    public static void main(String[] args) {
        HelloApi helloApi = new HelloApi();


        System.out.println(helloApi.hello("byte-buddy"));
        System.out.println(helloApi.hello("byte-buddy", LocalTime.now().toString()));
    }
}
