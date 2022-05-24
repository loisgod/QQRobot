package com.QQRobot.demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HScenceData {
    private int id;
    private String fileUrl;

    public HScenceData(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
