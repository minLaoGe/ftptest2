package com.example.ftptest2.enitity;

import com.example.ftptest2.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

public class EeventEntity {

    private List<String> keyWords;


    private String command;
    private String commandSuffix;
    private String target;
    private String name;
    private String desc;
    private String serverIp;
    private String exceptionCommand;


    public String getExceptionCommand() {
        return exceptionCommand;
    }

    public void setExceptionCommand(String exceptionCommand) {
        this.exceptionCommand = exceptionCommand;
    }

    public String getServerIp() {
        return serverIp;
    }
    public String getComplieName(){
        return serverIp+this.name;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getCommand() {
        if (command==null){
            return "tail -f ";
        }
        return command;
    }

    public List<String> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(List<String> keyWords) {
        this.keyWords = keyWords;
    }

    public static EeventEntity  getEventEntity(EventEmum eventEmum){
        EeventEntity eeventEntity = new EeventEntity();
        eeventEntity.setTarget(eventEmum.getTarget());
        eeventEntity.setDesc(eventEmum.getDesc());
        eeventEntity.setCommand(eventEmum.getCommand());
        eeventEntity.setName(eventEmum.getName());
        eeventEntity.setCommandSuffix(eventEmum.getCommandSuffix());
        eeventEntity.setKeyWords(eventEmum.getKeyWords());
        return  eeventEntity;
    }

    public String getTarget() {
        if (this.target.contains("&{date}")){
            String s = this.target.replaceAll("&\\{date}", DateUtil.formatDate(new Date(), DateUtil.yyyyMMdd));
            return s;
        }

        return this.target;
    }

    public void setTarget(String target) {
        this.target = target;
    }


 public String getCompliteComand(){
        if (!StringUtils.isEmpty(this.getCommandSuffix())){
            return this.getCommand()+this.getTarget()+this.getCommandSuffix() ;
        }else {
            return this.getCommand()+this.getTarget();
        }
 }
    public String getCommandSuffix() {
        if (StringUtils.isNotEmpty(this.commandSuffix)&&this.commandSuffix.contains("&{keyword}")){
            for (String keyWord : this.keyWords) {
                String s = this.commandSuffix.replace("&{keyword}", keyWord);
                return s;
            }
        }
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
