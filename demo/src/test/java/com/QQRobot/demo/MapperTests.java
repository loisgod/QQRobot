//package com.QQRobot.demo;
//
//import com.QQRobot.demo.dao.ConfigMapper;
//import com.QQRobot.demo.entity.ConfigData;
//import com.QQRobot.demo.entity.StudyData;
//import com.QQRobot.demo.dao.StudyDataMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ContextConfiguration;
//
//import java.util.*;
//
//@SpringBootTest
//@ContextConfiguration(classes = DemoApplication.class)
//public class MapperTests {
//
//    @Autowired
//    private StudyDataMapper studyDataMapper;
//
//    @Autowired
//    private ConfigMapper configMapper;
//
//    @Test
//    public void testConfigData(){
//        ConfigData configData = new ConfigData("狗","二哈","123");
//        configMapper.insertConfigData(configData);
//        configData = configMapper.selectConfigDataByKeyword("123","狗");
//        System.out.println(configData);
//        configMapper.updateConfigData("123","狗","三狗子");
//        configData = configMapper.selectConfigDataByKeyword("123","狗");
//        System.out.println(configData);
//    }
//
//
//}
