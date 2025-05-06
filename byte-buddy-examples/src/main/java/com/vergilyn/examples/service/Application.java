package com.vergilyn.examples.service;

import java.time.LocalTime;

public class Application {

    public static void main(String[] args) {

        HelloServiceImpl helloService = new HelloServiceImpl();
        System.out.println(helloService.hello("byte-buddy"));
        System.out.println(helloService.hello("byte-buddy", LocalTime.now().toString()));

    }

}
