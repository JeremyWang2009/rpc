package com.zfzj.service.service;

/**
 * @author jeremy.wang
 * @date 2019-11-04
 */
public class HelloServiceImpl implements HelloService {

    public String hello(String name) {
        return "hello".concat(" ").concat(name);
    }
}
