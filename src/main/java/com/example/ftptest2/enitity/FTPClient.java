package com.example.ftptest2.enitity;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@Getter
public  class FTPClient implements AbstractFTPClient {
    @Value("${ftp.witheListCommand}")
    private String[] witheListCommand;
    @Value("${ftp.host}")
    private String host;
    @Value("${ftp.username}")
    private String username;
    @Value("${ftp.password}")
    private String password;
    @Value("${ftp.src}")
    private String src;
    @Value("${ftp.filename}")
    private String filename;
    @Value("${ftp.port}")
    private Integer port;

    public FTPClient() {
    }
}
