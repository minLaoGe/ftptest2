package com.example.ftptest2.service;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.stereotype.Service;

public interface MessageHandler {
   void handleMessage(String mssage);
   void test(String mssage);
}
