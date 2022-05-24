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
//public class HttpTest {
//
//    @Test
//    void postTest() {
//        Map<String, Object > map =new HashMap<>();
//        map.put("message_type","group");
//        map.put("group_id",869735393);
//        map.put("message","[CQ:image,file=1.PNG,url=http://localhost:8400/1.PNG]");
//
//        HttpRequestUtil.doPost("http://localhost:5700/send_msg", JSON.toJSONString(map));
//    }
//
//}
