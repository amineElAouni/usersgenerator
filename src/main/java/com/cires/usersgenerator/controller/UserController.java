package com.cires.usersgenerator.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @RequestMapping("/generate")
    public String index(@RequestParam(value = "count", required = false) Integer count) {
        if(count != null) {
            return "Hello World :"+count;
        }
            return null;
    }
}
