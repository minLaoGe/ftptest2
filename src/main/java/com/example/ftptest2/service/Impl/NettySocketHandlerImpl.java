package com.example.ftptest2.service.Impl;

import com.corundumstudio.socketio.SocketIOServer;
import com.example.ftptest2.enitity.ChatObject;
import com.example.ftptest2.service.NettySocketServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class NettySocketHandlerImpl implements NettySocketServer {
    private  final SocketIOServer socketIOServer;
    @Override
    public void handleMessage(String message) {
       /* ChatObject chatObject = new ChatObject();
        chatObject.setMessage(message);
        chatObject.setUserName("user");*/

        socketIOServer.getBroadcastOperations().sendEvent("chatEvent", message +"                 " +
                "" +
                "\n" +
                "\n" +
                "");
//        test(message);


    }

    public void test(String message) {
        while (true){
            socketIOServer.getBroadcastOperations().sendEvent("chatEvent", message +"</br>");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
