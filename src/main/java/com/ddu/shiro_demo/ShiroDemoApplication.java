package com.ddu.shiro_demo;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class ShiroDemoApplication {

    public static void main(String[] args) {
//        ByteSource credentialsSalt = ByteSource.Util.bytes("user02");
//        Object result = new SimpleHash("MD5","123456",credentialsSalt,2);
//        System.out.println(result);
        SpringApplication.run(ShiroDemoApplication.class, args);
    }

}
