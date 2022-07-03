//package com.QQRobot.demo;
//
//
//import com.QQRobot.demo.Service.KatchaService;
//import com.QQRobot.demo.Utils.KatchaSystem;
//import com.QQRobot.demo.Utils.RedisKeyUtil;
//import com.QQRobot.demo.Utils.WordFilterUtil;
//import com.alibaba.fastjson.JSON;
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.test.context.ContextConfiguration;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@SpringBootTest
//@ContextConfiguration(classes = DemoApplication.class)
//public class KatchaTest {
//
//    @Autowired
//    RedisTemplate redisTemplate;
//
//    @Autowired
//    KatchaService katchaService;
//
//
//    @Test
//    public void testRandom() {
//        double base_probability = 0.006;
//        int start = 50;
//        int end = 90;
//        double incre_probability = (1-base_probability)/(end-start);
//        double probability = 0.006;
//        int last = 0;
//        int time=0;
//        int times = 10;
//        for (int i = 0; i < times; i++) {
//            double res = Math.random();
//
//            if (last  >= start) {
//                probability = base_probability + (last-start+1)*incre_probability;
//            }
//
//            if(res<probability) {
//                ++time;
//                last = 0;
//                probability = base_probability;
//            } else {
//                ++last;
//            }
//        }
//        System.out.println(time + " " + (double)time/(double)times);
//    }
//
//    @Test
//    public void testKatcha() {
//        List<String[] > list = katchaService.katcha("123","123","!23","223",new HashMap<String ,Object>(),10,false);
//        Map<String ,Object> map = JSON.parseObject(list.get(0)[1]);
//        String  l = (String) map.get("message");
//        System.out.println(l);
////        for(String s:l) {
////            System.out.print(s+ " ");
////        }
//
//
//    }
//    @Test
//    public void addRedis() {
//        String RedisKey = RedisKeyUtil.getKatcha("test","test");
//        redisTemplate.opsForHash().put(RedisKey,"base_probability",(double)0.006);
//        redisTemplate.opsForHash().put(RedisKey,"SR_probability",0.05);
//        redisTemplate.opsForHash().put(RedisKey,"baodiTime",90);
//        redisTemplate.opsForHash().put(RedisKey,"incrTime",73);
//
//        double res = (double) redisTemplate.opsForHash().get(RedisKey,"SR_probability");
//        System.out.println(res);
//    }
//
//    @Test
//    public void testAdd() {
//        List<String[] > list = katchaService.addCard("#添加三星 比利 黑哥哥","123","223",new HashMap<String ,Object>());
//        Map<String ,Object> map = JSON.parseObject(list.get(0)[1]);
//        String  l = (String) map.get("message");
//        System.out.println(l);
//    }
//
//    @Test
//    public void testDelete() {
//        List<String[] > list = katchaService.deleteCard("#删除五星 远野 我修院","123","223",new HashMap<String ,Object>());
//        Map<String ,Object> map = JSON.parseObject(list.get(0)[1]);
//        String  l = (String) map.get("message");
//        System.out.println(l);
//    }
//
//    @Test
//    public void testShow() {
//        List<String[] > list = katchaService.showCard("#删除五星 远野 我修院","123","223",new HashMap<String ,Object>());
//        Map<String ,Object> map = JSON.parseObject(list.get(0)[1]);
//        String  l = (String) map.get("message");
//        System.out.println(l);
//    }
//
//
//
//}
//
//
