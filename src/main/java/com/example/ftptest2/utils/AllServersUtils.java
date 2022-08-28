package com.example.ftptest2.utils;


import com.example.ftptest2.Ftptest2Application;
import com.example.ftptest2.enitity.FTPClient;
import com.example.ftptest2.enitity.FTPLogin;
import freemarker.core.Environment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.system.SystemProperties;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AllServersUtils {
    private static final HashMap<String, FTPClient> hashMap=new HashMap<>();
    private static final List<String> witheList= Arrays.asList("tail -200f","tail -f");

    private static  final HashMap<Integer,FTPClient> orderHashMap=new HashMap<>();
    static {

        for (int i = 0; i < 100; i++) {

            String host = RawConfigUtil.getString("ftp" + i+".host");
            if(StringUtils.isEmpty(host)){
                break;
            }
            String port = RawConfigUtil.getString("ftp" + i+".port");
            String username = RawConfigUtil.getString("ftp" + i+".username");
            String password = RawConfigUtil.getString("ftp" + i+".password");
            String src = RawConfigUtil.getString("ftp" + i+".src");
            String filename = RawConfigUtil.getString("ftp" + i+".filename");
            String witheListCommand = RawConfigUtil.getString("ftp" + i+".witheListCommand");
            FTPClient ftpClient = new FTPClient();
            if (StringUtils.isEmpty(witheListCommand)){
                ftpClient.setWitheListCommand(witheList);
            }else {
                String[] split = witheListCommand.split(",");
                List<String> lastWithList=Arrays.asList(split);
                List<String> collect = witheList.stream().filter(s -> {
                    if (lastWithList.contains(s)) {
                        return false;
                    } else {
                        return true;

                    }
                }).collect(Collectors.toList());
                collect.addAll(lastWithList);
                ftpClient.setWitheListCommand(collect);
            }

            ftpClient.setHost(host);
            ftpClient.setPort(Integer.parseInt(port));
            ftpClient.setUsername(username);
            ftpClient.setPassword(password);
            ftpClient.setSrc(src);
            ftpClient.setFilename(filename);
            hashMap.put(ftpClient.getHost()+ftpClient.getUsername(),ftpClient);
            orderHashMap.put(i,ftpClient);

        }
    }


    public  static  FTPClient getFtpClient(int index){
        return orderHashMap.get(index);
    }
}
