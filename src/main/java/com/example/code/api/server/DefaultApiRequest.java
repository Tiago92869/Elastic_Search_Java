package com.example.code.api.server;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@RestController
public class DefaultApiRequest {

    @GetMapping(value = "/welcome")
    public String welcome(){
        System.out.println(StringUtils.join("Hello", " this is", " Spring boot", " REST API"));
        return "Welcome to Spring Boot";
    }

    @GetMapping(value = "/time")
    public String time() {
        return LocalTime.now().toString();
    }
}
