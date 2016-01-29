package com.shangpin.iog.common.utils.logger;

/**
 * Created by lizhongren on 2016/1/26.
 */
public class LoggerMessage {

    private String dateTime;//时间
    private String level;//级别
    private String ip;//IP
    private String project;  //项目
    private String module;   //模块
    private String function;//功能
    private String message; //消息


    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void clean(){
        this.project="";
        this.module="";
        this.function="";
        this.message="";
        this.dateTime="";
        this.level="";
        this.ip="";
    }
    @Override
    public String toString() {
        return "{\"dateTime\":\"" + dateTime + '"' +
                ",\"level\":\"" + level + '"' +
                ",\"ip\":\"" + ip + '"' +
                ",\"project\":\"" + project + '"' +
                ",\"module\":\"" + module + '"' +
                ",\"function\":\"" + function + '"' +
                ",\"message\":\"" + message + '"' +

                '}';
    }
}
