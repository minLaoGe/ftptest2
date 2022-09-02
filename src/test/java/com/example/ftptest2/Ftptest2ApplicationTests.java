package com.example.ftptest2;

import com.corundumstudio.socketio.SocketIOServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Ftptest2ApplicationTests {
    @Autowired
    private SocketIOServer ioServer;
    @Test
    void contextLoads() {
        ioServer.stop();
    }

}
