package com.example.ftptest2.enitity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Accessors(chain = true)
public class FTPLogin {

    private String remotehost;
    private Integer port;
    private String username;
    private String passsword;
    private String src;
    private String filename;
}
