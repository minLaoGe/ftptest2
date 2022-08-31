package com.example.ftptest2.utils;

import com.example.ftptest2.enitity.FTPConfigAdopt;

public class CommonUtils {
    public static String getHostNameAndUsername(FTPConfigAdopt ftpConfigAdopt){
        return ftpConfigAdopt.getFtpLogin().getRemotehost()+ftpConfigAdopt.getFtpLogin().getUsername();
    }
}
