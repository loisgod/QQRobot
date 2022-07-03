package com.QQRobot.demo.Service;


import com.QQRobot.demo.Utils.ConstantUtil;
import com.QQRobot.demo.Utils.HttpRequestUtil;
import com.QQRobot.demo.entity.Event;
import com.QQRobot.demo.event.EventProducer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CQService implements ConstantUtil {

    @Autowired
    EventProducer eventProducer;

    public String sendGroupMessage(Map<String ,String > info,String message) {
        String url = "http://localhost:5700/send_group_msg";
        Map<String,Object > map = new HashMap<>();
        map.put("group_id",info.get("group_id"));
        map.put("message",message);
        String jsonStr = JSON.toJSONString(map);
        Event event = new Event()
                .setTopic(TOPIC_SEND)
                .setUrl(url)
                .setMessage(jsonStr)
                .setData("self_id",info.get("self_id"))
                .setData("group_id",info.get("group_id"));
        eventProducer.fireEvent(event);
        return "";
    }

    public String getGroupMessage(Map<String,String> info,String id) {
        String url = "http://localhost:5700/get_msg";
        Map<String ,Object> map = new HashMap<>();
        map.put("message_type","group");
        map.put("group_id",info.get("group_id"));
        map.put("message_id",id);
        String res = HttpRequestUtil.doPost(url,JSON.toJSONString(map));
        JSONObject r = (JSONObject) JSON.parseObject(res,HashMap.class).get("data");
        return (String) r.get("message");
    }

    public String deleteGroupMessage(Map<String ,String > info,String message_id) {
        String url = "http://localhost:5700/delete_msg";
        Map<String,Object > map = new HashMap<>();
        map.put("message_id",message_id);
        String jsonStr = JSON.toJSONString(map);
        Event event = new Event()
                .setTopic(TOPIC_SEND)
                .setUrl(url)
                .setMessage(jsonStr);
        eventProducer.fireEvent(event);
        return "";
    }




}
