package com.ibm.devopscoc.showcasefail.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping("/")
    String home() {
        return "This application has been deployed via a fully automated toolchain";
    }

}
