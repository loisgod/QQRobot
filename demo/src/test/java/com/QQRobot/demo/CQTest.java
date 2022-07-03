//package com.QQRobot.demo;
//
//import com.QQRobot.demo.Utils.CqUtil;
//import com.QQRobot.demo.Utils.DataUtil;
//import com.QQRobot.demo.Utils.HttpRequestUtil;
//import com.QQRobot.demo.Utils.WordFilterUtil;
//import com.alibaba.fastjson.JSON;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ContextConfiguration;
//
//import javax.script.Invocable;
//import javax.script.ScriptEngine;
//import javax.script.ScriptEngineManager;
//import javax.script.ScriptException;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.regex.Pattern;
//
//@SpringBootTest
//@ContextConfiguration(classes = DemoApplication.class)
//public class CQTest {
//
//    @Test
//    public void mathchingTest() {
////        String content = "I am noob" + "from runoob.com.";
////        String pattern = ".*runoob.*";
////        boolean isMatch = Pattern.matches(pattern,content);
////        System.out.println(isMatch);
//        String content = "   " +
//                "" +
//                "I  B C  \n " +
//                "D  \n" +
//                " Q \n " +
//                "\n" +
//                " \n";
//        content = content.replaceAll("\\s+"," ");
//        content = content.trim();
//
//        System.out.println(content);
//    }
//
//    @Test
//    public void SperMatchingTest() {
//        String[] important = "火影忍者".split("");
//        String pattern = ".*";
//        String content = "欧洲大火影响了忍界影分身比赛者了";
//        for(String s: important) {
//            pattern = pattern + s + ".*";
//        }
//        System.out.println(pattern);
//        System.out.println(Pattern.matches(pattern,content));
//    }
//
//    @Test
//    public void CqImageMathingTest() {
//        String content = "  [CQ:image,file=0b875d99d6c6e73f93da816d313c2f02.image,url=https://gchat.qpic.cn/gchatpic_new/308006378/588696648-2847178461-0B875D99D6C6E73F93DA816D313C2F02/0?term=3,subType=0]  [CQ:image,file=file:///1.PNG,url=http://localhost:8400/1.PNG] " +
//                "  [CQ:image,file=file:///2.PNG,url=http://localhost:8400/2.PNG] ";
//        CqUtil.CQImagedecoder(content);
//    }
//    @Test
//    public void CqDownloardTest() {
//        String url = "https://gchat.qpic.cn/gchatpic_new/308006378/588696648-2847178461-0B875D99D6C6E73F93DA816D313C2F02/0?term=3,subType=0";
//        String path = "c:/images";
//        System.out.println(CqUtil.CqDownloardImages(url,path));
//
//    }
//
//    @Test
//    public void CqEncodeTest() {
//        String CQ= String.format("[CQ:%s,file=%s,url=%s]", "image","file:///c:/images/1653132382548.gif","file:///c:/images/1653132382548.gif");
//        System.out.println(CQ);
//        String url = "http://localhost:5700/send_msg";
//        Map<String, Object > map =new HashMap<>();
//        map.put("message_type","group");
//        map.put("group_id",588696648);
//        map.put("message",CQ);
//        HttpRequestUtil.doPost(url, JSON.toJSONString(map));
//    }
//
//    @Test
//    public void MatchTest() {
//        String message = "#学习全局 哈哈哈";
//        String[] res = DataUtil.decoder("#学习",message);
//        for(String s:res){
//            System.out.println("Test:" + s);
//        }
//    }
//
//    @Test
//    public void WordFilterTest() {
//        WordFilterUtil wordFilterUtil = new WordFilterUtil();
//        wordFilterUtil.addWord("苟");
//        wordFilterUtil.addWord("阿巴阿");
//        wordFilterUtil.addWord("啦啦啦");
//        wordFilterUtil.addWord("呱呱呱");
//        wordFilterUtil.addWord("吱吱吱");
//        wordFilterUtil.addWord("嘻嘻嘻");
//        long start;
//        long end;
//        String s = "阿巴阿巴歪比巴布苟";
//        start = System.nanoTime();
//        wordFilterUtil.checkKeyWord(s);
//        end = System.nanoTime();
//        System.out.println((end-start));
//        start = System.nanoTime();
//        s.contains("苟");
//        s.contains("阿巴阿");
//        s.contains("啦啦啦");
//        s.contains("呱呱呱");
//        s.contains("吱吱吱");
//        s.contains("嘻嘻嘻");
//        end = System.nanoTime();
//        System.out.println((end-start));
//    }
//
//}
