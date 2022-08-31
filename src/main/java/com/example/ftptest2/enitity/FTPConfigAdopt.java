package com.example.ftptest2.enitity;


import com.example.ftptest2.utils.DateUtil;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class FTPConfigAdopt extends ExecuteFTP {

//    @Value("${ftp.witheListCommand}")

    private FTPLogin ftpLogin;

    private Session session;
    private ConcurrentHashMap<String,Channel> hashMap=new ConcurrentHashMap<>();

    private List<String> whiteCommandList;
    private int reconnectCount=0;
    private int reExecuCommandCount=0;





    public FTPConfigAdopt(AbstractFTPClient abstractFTPClient) {

        ftpLogin=new FTPLogin().setSrc(abstractFTPClient.getSrc().replace("date", DateUtil.formatDate(new Date(),DateUtil.yyyyMMdd)))
                .setPasssword(abstractFTPClient.getPassword()).setFilename(abstractFTPClient.getFilename())
                .setPort(abstractFTPClient.getPort()).setRemotehost(abstractFTPClient.getHost())
                .setUsername(abstractFTPClient.getUsername());
        this.whiteCommandList= abstractFTPClient.getWitheListCommand();
    }



}
