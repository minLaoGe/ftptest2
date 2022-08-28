package com.example.ftptest2.enitity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
public class FtpCommand {
    private String command;
    private String commandSuffix;
    private List<String> whiteCommandList;

    private String Event;

    private FTPLogin ftpLogin;
    public FtpCommand(FTPLogin ftpLogin) {
        this.ftpLogin=ftpLogin;
    }

    public void setCommand(String command) {
        if (!whiteCommandList.contains(command.trim())){
            throw new  RuntimeException("非法命令");
        };

        this.command = command;
    }



    public String getCommand() {
        String comman=command+ftpLogin.getSrc()+ftpLogin.getFilename();
        if (StringUtils.isNotEmpty(this.commandSuffix)){
            comman= comman+commandSuffix;
        }
        return comman;
    }

}
