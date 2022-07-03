package com.QQRobot.demo.event;

import com.QQRobot.demo.Service.QQRobotService;
import com.QQRobot.demo.Utils.ConstantUtil;
import com.QQRobot.demo.Utils.HttpRequestUtil;
import com.QQRobot.demo.Utils.RedisKeyUtil;
import com.QQRobot.demo.entity.Event;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EventConsumer implements ConstantUtil {

    @Autowired
    private QQRobotService qqRobotService;

    @Autowired
    private RedisTemplate redisTemplate;

    @KafkaListener(topics = {TOPIC_SERVICE})
    public void handleService(ConsumerRecord record) {
        if(record == null || record.value() == null) {
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(),Event.class);
        if(event == null) {
            return;
        }
        JSONObject jsonParam = event.getJson();
        qqRobotService.QQRobotEvenHandle(jsonParam);
    }

    @KafkaListener(topics = {TOPIC_SEND})
    public void handleSend(ConsumerRecord record) {
        if(record == null || record.value() == null) {
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(),Event.class);
        if(event == null) {
            return;
        }
        String message = event.getMessage();
        String url = event.getUrl();
        String result = HttpRequestUtil.doPost(url,message);
        if(result == null) return;
        System.out.println("result  " + result);
        JSONObject r = (JSONObject) JSON.parseObject(result).get("data");
        if(r == null) return;
        int message_id = (Integer) r.get("message_id");
        System.out.println("result: " + message_id);
        String RedisKey = RedisKeyUtil.getLastInfo(event.getData("group_id"),event.getData("self_id"));
        redisTemplate.opsForList().rightPush(RedisKey,message_id);
        if(redisTemplate.opsForList().size(RedisKey)==REDIS_LIST_MAX_SIZE) {
            redisTemplate.opsForList().leftPop(RedisKey);
        }

    }


}
