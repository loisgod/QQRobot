package com.QQRobot.demo.entity;

import lombok.Data;

import java.util.Date;

@Data
public class RememberData {
    private int id;
    private String keyword;
    private String content;
    private String comeFrom;
    private String userId;
    private Date createTime;

    public RememberData(String keyword,String content,String comeFrom,String userId) {
        this.keyword = keyword;
        this.content = content;
        this.comeFrom = comeFrom;
        this.userId = userId;
        this.createTime = new Date();
    }
}
