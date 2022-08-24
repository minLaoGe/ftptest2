package com.example.ftptest2.enitity;


import com.example.ftptest2.utils.DateUtil;
import lombok.Getter;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Getter
public class FTPConfigAdopt extends ExecuteFTP {

//    @Value("${ftp.witheListCommand}")

    private FTPLogin ftpLogin;

    private String command;

    private List<String> whiteCommandList;


    public FTPConfigAdopt(AbstractFTPClient abstractFTPClient) {

        ftpLogin=new FTPLogin().setSrc(abstractFTPClient.getSrc().replace("date", DateUtil.formatDate(new Date(),DateUtil.yyyyMMdd)))
                .setPasssword(abstractFTPClient.getPassword()).setFilename(abstractFTPClient.getFilename())
                .setPort(abstractFTPClient.getPort()).setRemotehost(abstractFTPClient.getHost())
                .setUsername(abstractFTPClient.getUsername());
        String[] witheListCommand = abstractFTPClient.getWitheListCommand();
        this.whiteCommandList=Arrays.asList(witheListCommand);
    }

    public void setCommand(String command) {
        if (!whiteCommandList.contains(command.trim())){
            throw new  RuntimeException("非法命令");
        };
        this.command = command;
    }



    public String getCommand() {
        return command+ftpLogin.getSrc()+ftpLogin.getFilename();
    }
}
