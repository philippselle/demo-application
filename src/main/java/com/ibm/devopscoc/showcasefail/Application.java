package com.ibm.devopscoc.showcasefail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan
public class Application {

    public String publicString;
    public int zero = 0;

    // public String noReturn(String fail) {
    //     boolean failure = true;
    //     while (failure) {
    //         //nothing
    //         failure = false;
    //     }
    //     return "fail";
    // }
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


}