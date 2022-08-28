package com.example.ftptest2.enitity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public  class FTPClient implements AbstractFTPClient {
    private List<String> witheListCommand;
    private String host;
    private String username;
    private String password;
    private String src;
    private String filename;
    private Integer port;
    private Integer reconnectCount;
    public FTPClient() {
    }

    public List<String> getWitheListCommand() {
        return witheListCommand;
    }
}
