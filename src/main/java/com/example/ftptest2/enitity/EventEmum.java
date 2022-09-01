package com.example.ftptest2.enitity;

import com.example.ftptest2.utils.DateUtil;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


public enum EventEmum {

//    CHAT_EVENT(null,"tail -200f ","|grep 发送短信","/home/app/logs/mobilemsg/&{date}/mobilemsg/msg.log","chatEvent","支付duanx"),
    CHAT_EVENT(null,"tail -200f ",null,"/home/app/logs/mobilemsg/&{date}/mobilemsg/msg.log","chatEvent","支付duanx"),
    LOGIN_EVENT(Arrays.asList("13020162922"),"tail -200f ","|grep --line-buffer &{keyword}","/home/app/logs/unifyuser/unifyUser/root.log","loginEvent","登录短信")
//    LOGIN_EVENT(null,"tail -20000f ",null,"/home/app/logs/unifyuser/unifyUser/root.log","loginEvent","登录短信")
    ;

    private List<String> keyWords;

    public List<String> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(List<String> keyWords) {
        this.keyWords = keyWords;
    }

    private String command;
    private String commandSuffix;
    private String target;
    private String name;
    private String desc;
    public String getCommand() {
        if (command==null){
            return "tail -f ";
        }
        return command;
    }

    public String getTarget() {
        if (this.target.contains("&{date}")){
            String s = this.target.replaceAll("&\\{date}", DateUtil.formatDate(new Date(), DateUtil.yyyyMMdd));
            return s;
        }
        if (this.target.contains("&{keyword}")){
            for (String keyWord : this.keyWords) {
                String s = this.target.replace("&\\{keyword}", keyWord);
            }
        }
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    EventEmum(List<String> keyWord, String command, String commandSuffix, String target, String name, String desc) {
        this.keyWords = keyWord;
        this.command = command;
        this.commandSuffix = commandSuffix;
        this.target = target;
        this.name = name;
        this.desc = desc;
    }


    public String getCommandSuffix() {
        return commandSuffix;
    }

    public void setCommandSuffix(String commandSuffix) {
        this.commandSuffix = commandSuffix;
    }

    public void setCommand(String command) {
        this.command = command;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
