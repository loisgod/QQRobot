//package com.QQRobot.demo;
//
//import com.alibaba.fastjson.JSON;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ContextConfiguration;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@SpringBootTest
//@ContextConfiguration(classes = DemoApplication.class)
//public class JsonTest {
//
//    @Test
//    public void fastJsonTest() {
//        Map<String, Object > map =new HashMap<>();
//        map.put("user_id","308006378");
//        map.put("message","Hello");
//        String jsonOutput = JSON.toJSONString(map);
//        Map<String ,Object> newMap = JSON.parseObject(jsonOutput,Map.class);
//        System.out.println(map.toString());
//        System.out.println(jsonOutput);
//        System.out.println(newMap);
//        System.out.println(JSON.parse(jsonOutput));
//    }
//
//    @Test
//    public void StringTest() {
//        String test="file=0b875d99d6c6e73f93da816d313c2f02.image,url=https://gchat.qpic.cn/gchatpic_new/308006378/588696648-2847178461-0B875D99D6C6E73F93DA816D313C2F02/0?term=3,subType=0";
//        System.out.println(JSON.parseObject(test));
//    }
//
//    @Test
//    public void MapTest() {
//        Map<String ,Map<String ,Object>> map = new HashMap<>();
//        Map<String,Object> temp1 = new HashMap<>();
//        map.put("a",temp1);
//        System.out.println(map);
//        temp1.put("bbb","Ccc");
//        System.out.println(map);
//        temp1 = map.get("a");
//        temp1.put("ccc","Ddd");
//        System.out.println(map);
//    }
//
//
//    @Test
//    public void IntegerTest() {
//        System.out.println(Integer.parseInt("dfad"));
//    }
//
//}
