package com.QQRobot.demo.Service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

public interface QQRobotService {
    void QQRobotEvenHandle(JSONObject jsonParam);
    public JSONObject getJSONParam(HttpServletRequest request);
    }
