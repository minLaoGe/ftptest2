package com.example.ftptest2.config.nettysocket;

import com.corundumstudio.socketio.SocketIOServer;
import com.example.ftptest2.config.netty.WsServer;
import com.example.ftptest2.controller.IndexController;
import com.example.ftptest2.service.NettySocketServer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class NettySocketInitConfig  implements ApplicationListener<ContextRefreshedEvent>, DisposableBean {
    @Autowired
    private SocketIOServer ioServer;
    @Autowired
    private NettySocketServer socketServer;
    @Autowired
    private IndexController indexController;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null){
            ioServer.start();
       /*     Thread thread = new Thread(()->{
                socketServer.test("ioServer");
            });
            thread.start();*/
            indexController.beginWrite();
        }
    }

    @Override
    public void destroy() throws Exception {
        ioServer.stop();
    }
}
