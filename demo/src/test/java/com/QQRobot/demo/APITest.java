//package com.QQRobot.demo;
//
//import com.QQRobot.demo.Utils.HttpRequestUtil;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
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
//        String url = "http://localhost:5700/get_msg";
//        map.put("message_type","group");
//        map.put("group_id",869735393);
//        map.put("message_id",-563013254);
//        String res = HttpRequestUtil.doPost(url,JSON.toJSONString(map));
//        JSONObject map = (JSONObject) JSON.parseObject(res,HashMap.class).get("data");
//        System.out.println(map.get("message"));
//    }
//
//
//
//    @Test
//    public void DeleteTest() {
//        int msg_id = -1260715592;
//        String url = "http://localhost:5700/delete_msg";
//        map.put("message_id",msg_id);
//        HttpRequestUtil.doPost(url,JSON.toJSONString(map));
//    }
//}
