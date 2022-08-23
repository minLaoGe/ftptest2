package com.example.ftptest2.controller;

import com.example.ftptest2.config.netty.ChatHandler;
import com.example.ftptest2.utils.FtpTestClient;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
@Slf4j
public class IndexController {
    @GetMapping("/index.html")
    public String toIndex(){
        return "html/a.html";
    }
    public static void beginWrite(){
        FtpTestClient.begin();
    }
    public static void sendMess(){
        log.info("开始打印消息");
        while (true){
            for (Channel client : ChatHandler.clients) {
                log.info("开始发生短信");
                client.writeAndFlush("sdf");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
