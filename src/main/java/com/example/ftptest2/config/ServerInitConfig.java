package com.example.ftptest2.config;

import com.example.ftptest2.config.netty.WsServer;
import com.example.ftptest2.controller.IndexController;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * netty服务端启动加载配置
 */

//@Component
public class ServerInitConfig implements ApplicationListener<ContextRefreshedEvent> {
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(event.getApplicationContext().getParent() == null){
			IndexController. sendMess();
		}
	}
}
