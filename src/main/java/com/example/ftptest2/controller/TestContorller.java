package com.example.ftptest2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("")
@Controller
public class TestContorller {
    @GetMapping("/test")
    @ResponseBody
    public String sdf(){
        return "你好啊";
    }
    @GetMapping("/sfrzfw/test")
    @ResponseBody
    public String socket(){
        return "你好啊2222";
    }
 }
