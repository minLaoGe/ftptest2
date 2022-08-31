package com.example.ftptest2.config.nettysocket;

import com.corundumstudio.socketio.AckCallback;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.VoidAckCallback;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.example.ftptest2.enitity.ChatObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import java.util.UUID;

@Configuration
@Slf4j
public class NeetyConfiguration {
    @Value("${socket.host}")
    public String host;

    @Value("${socket.port}")
    public Integer port;

    @Autowired
    private   SocketIOServer socketIOServer;
      @Bean
      public SocketIOServer getServers(DataListener<ChatObject> obj,ConnectListener connectListener){
          com.corundumstudio.socketio.Configuration configuration = new com.corundumstudio.socketio.Configuration();
          configuration.setHostname(host);
          configuration.setPort(port);
          SocketIOServer socketIOServer = new SocketIOServer(configuration);
          socketIOServer.addEventListener("ackevent1",ChatObject.class,obj);
          socketIOServer.addConnectListener(connectListener);

          return socketIOServer;
      }
      @Bean
      public ConnectionEventListener getConnectEventListener(){
          return new ConnectionEventListener() {
              @Override
              public void connectionClosed(ConnectionEvent event) {
                  log.warn("客户端断开链接,sessionId为:{}",event.getSource());
              }

              @Override
              public void connectionErrorOccurred(ConnectionEvent event) {

              }
          };
      }
      @Bean
      public ConnectListener getConnectListener(){
          return  new ConnectListener() {
              @Override
              public void onConnect(SocketIOClient socketIOClient) {
                  int size = socketIOServer.getAllClients().size();
                  UUID sessionId = socketIOClient.getSessionId();
                    log.info("客户端建立连接id为:{};ip为:{};至今一共有{}个客户端链接",sessionId.toString(),socketIOClient.getRemoteAddress(),size);
              }
          };
      }
    @Bean
    public DataListener<ChatObject> getEvenetListener(){
          return (client,data,ackRequest)->{

              // check is ack requested by client,
              // but it's not required check
              if (ackRequest.isAckRequested()) {
                  // send ack response with data to client
                  ackRequest.sendAckData("client message was delivered to server!", "yeah!");
              }

              // send message back to client with ack callback WITH data
              ChatObject ackChatObjectData = new ChatObject(data.getUserName(), "message with ack data");
              client.sendEvent("ackevent2", new AckCallback<String>(String.class) {
                  @Override
                  public void onSuccess(String result) {
                      System.out.println("ack from client: " + client.getSessionId() + " data: " + result);
                  }
              }, ackChatObjectData);

              ChatObject ackChatObjectData1 = new ChatObject(data.getUserName(), "message with void ack");
              client.sendEvent("ackevent3", new VoidAckCallback() {

                  protected void onSuccess() {
                      System.out.println("void ack from: " + client.getSessionId());
                  }

              }, ackChatObjectData1);
          };
    }

}


