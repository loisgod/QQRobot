package com.QQRobot.demo.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class StudyData {
    private int id;
    private String keyword;
    private String content;
    private String comeFrom;
    private String teacher;
    private Date createTime;
    private int crazy;

    private int active;

//    public StudyData(){}
    public StudyData(String keyword,String content,String comeFrom,String teacher,int crazy,Date createTime,int active) {
        this.keyword = keyword;
        this.content = content;
        this.comeFrom = comeFrom;
        this.teacher = teacher;
        this.crazy = crazy;
        this.createTime = createTime;
        this.active = active;
    }
}
