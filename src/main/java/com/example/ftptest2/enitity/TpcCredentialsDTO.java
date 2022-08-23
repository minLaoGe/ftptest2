package com.example.ftptest2.enitity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TpcCredentialsDTO {
    private String username;
    private int port;
    private String password;
    private String host;
    private String fileServersPath;
    private String fileServersCommand;
    private String localPath;
}
