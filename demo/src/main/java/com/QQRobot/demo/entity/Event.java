package com.QQRobot.demo.entity;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Event {

    private String topic;
    private String url;
    private String message;
    private Map<String ,Object> data = new HashMap<>();
    private JSONObject json;

    public JSONObject getJson() {
        return json;
    }

    public Event setJson(JSONObject json) {
        this.json = json;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Event setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Event setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }


    public Map<String, Object> getData() {
        return data;
    }
    public String getData(String key) {
        return (String) data.get(key);
    }

    public Event setData(String key, Object value) {
        this.data.put(key,value);
        return this;
    }

}
