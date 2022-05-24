//package com.QQRobot.demo;
//
//import com.QQRobot.demo.Utils.HttpRequestUtil;
//import com.alibaba.fastjson.JSON;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ContextConfiguration;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@SpringBootTest
//@ContextConfiguration(classes = DemoApplication.class)
//public class APITest {
//
//    Map<String, Object > map =new HashMap<>();
//
//    @Test
//    public void PostTest() {
//        String url = "http://localhost:5700/send_msg";
//        map.put("message_type","group");
//        map.put("group_id",588696648);
//        map.put("message","[CQ:image,file=5d323f3594f75ff7901d3716846e1710.image,url=https://gchat.qpic.cn/gchatpic_new/308006378/869735393-2492793543-5D323F3594F75FF7901D3716846E1710/0?term=3,subType=1]");
//        HttpRequestUtil.doPost(url,JSON.toJSONString(map));
//
//    }
//
//    @Test
//    public void DeleteTest() {
//        int msg_id = -1260715592;
//        String url = "http://localhost:5700/delete_msg";
//        map.put("message_id",msg_id);
//        HttpRequestUtil.doPost(url,JSON.toJSONString(map));
//    }
//}
