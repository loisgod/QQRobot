//package com.QQRobot.demo;
//
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
//    @Test
//    public void selectKeyTest() {
//        List<StudyData> list = studyDataMapper.selectByKey("869735393","阿巴阿巴");
//        for(StudyData studyData:list) {
//            System.out.println(studyData);
//        }
//    }
//
//    @Test
//    public void TestInsertStudyData() {
//        StudyData studyData = new StudyData();
//        studyData.setComeFrom("588696648");
//        studyData.setTeacher("308006378");
//        studyData.setKeyword("彭格列");
//        studyData.setContent("毁灭也好，繁荣也好....随你喜欢，十代目");
//        studyData.setCrazy(2);
//        studyData.setCreateTime(new Date());
//        int rows = studyDataMapper.insertStudyData(studyData);
//
////        studyData = new StudyData();
////        studyData.setComeFrom("588696648");
////        studyData.setTeacher("308006378");
////        studyData.setKeyword("彭格列");
////        studyData.setContent("");
////        studyData.setCrazy(2);
////        studyData.setCreateTime(new Date());
////        rows = studyDataMapper.insertStudyData(studyData);
////
////        studyData = new StudyData();
////        studyData.setComeFrom("588696648");
////        studyData.setTeacher("308006378");
////        studyData.setKeyword("彭格列");
////        studyData.setContent("初代的");
////        studyData.setCrazy(2);
////        studyData.setCreateTime(new Date());
////        rows = studyDataMapper.insertStudyData(studyData);
//
//    }
//
//    @Test
//    public void TestSelectStudyData(){
//        List<StudyData> list = studyDataMapper.selectByKey("588696648","学狗叫，三回啊三回");
//        for(StudyData studyData:list) {
//            System.out.println(studyData);
//        }
//    }
//    @Test
//    public  void TestCrazy() {
//        String message="影为华火";
//        String comeFrom="588696648";
//        List<StudyData> list = studyDataMapper.selectByCrazy(1,comeFrom);
//        for (StudyData s:list) {
//            System.out.println(s);
//        }
//        List<String> res = new ArrayList<>();
//        if(list==null) {
//            return ;
//        }
//        Set<String> set = new HashSet<>();
//        out:for(StudyData studyData:list) {
//            String[] s = studyData.getKeyword().split("");
//            for(String c:s) {
//                if(!message.contains(c)) {
//                    continue out;
//                }
//            }
//            set.add(studyData.getKeyword());
//        }
//        for (String s:set) {
//            System.out.println(s);
//            List<StudyData> l = studyDataMapper.selectByKey(comeFrom,s);
//            res.add(l.get(new Random().nextInt(l.size())).getContent());
//        }
//        for(String s:res) {
//            System.out.println(s);
//        }
//    }
//
//
//}
