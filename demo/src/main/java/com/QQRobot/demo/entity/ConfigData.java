package com.QQRobot.demo.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ConfigData {
    private int id;
    private String keyword;
    private String content;
    private String comeFrom;
    private Date createTime;

    public ConfigData(String keyword, String content, String comeFrom) {
        this.keyword = keyword;
        this.content = content;
        this.comeFrom = comeFrom;
        this.createTime = new Date();
    }
}
