package com.hechi.niumall.controller;

import com.hechi.niumall.entity.User;
import com.hechi.niumall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {
    @Autowired
    UserService userService;
    @RequestMapping("/test")
    public User getUser(){
       return userService.getUser();
    }
}
