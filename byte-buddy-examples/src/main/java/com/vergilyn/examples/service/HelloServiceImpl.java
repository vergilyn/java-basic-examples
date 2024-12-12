package com.vergilyn.examples.service;

public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        return "Hello: " + name;
    }

    @Override
    public String hello(String name, String message) {
        return "Hello: " + name + ", msg: " + message;
    }

}
