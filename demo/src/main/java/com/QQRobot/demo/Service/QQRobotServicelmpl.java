package com.QQRobot.demo.Service;

import com.QQRobot.demo.Utils.ConstantUtil;
import com.QQRobot.demo.Utils.DataUtil;
import com.QQRobot.demo.Utils.HttpRequestUtil;
import com.QQRobot.demo.Utils.RedisKeyUtil;
import com.QQRobot.demo.dao.StudyDataMapper;
import com.QQRobot.demo.entity.StudyData;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;
import javafx.geometry.Pos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service
@Slf4j
public class QQRobotServicelmpl implements QQRobotService, ConstantUtil {

    @Autowired
    private StudyDataMapper studyDataMapper;

    private Map<String ,Object> user_status = new HashMap<>();
    @Autowired
    private AutoReapeatService autoReapeatService;
    @Autowired
    private HSenceService hSenceService;
    @Autowired
    private KatchaService katchaService;

    @Autowired
    private RedisTemplate redisTemplate;



    @Override
    public void QQRobotEvenHandle(JSONObject jsonParam) {
        //JSONObject
//        JSONObject jsonParam = this.getJSONParam(request);
        log.info("接收参数为:{}",jsonParam.toString() !=null ? "SUCCESS:" + jsonParam.toString() : "FALSE");
        if("message".equals(jsonParam.getString("post_type"))){
            Map<String,String > info = getMessageInfo(jsonParam);
            if("group".equals(info.get("message_type"))) {
                List<String []> Posts = new ArrayList<>();
                String message = info.get("message");
                String group_id = info.get("group_id");
                String user_id = info.get("user_id");
                if(katchaService.KatchaSystem(info)) {

                } else if(message.equals("#瑟图总数")){
                    hSenceService.HScenceCounter(info);
                } else if (message.contains("#撤回") || message.contains("#烦诶")) {
                    autoReapeatService.deleteInfo(info);
                } else if(message.equals("#帮助")){
                    autoReapeatService.Help(info);
                } else if(message.contains("#说话")) {
                    autoReapeatService.speak(info);
                } else if(hSenceService.checkSuperHScence(message)) {
                    user_status.put(info.get("user_id"),HSCENCE_INSERT);
                    hSenceService.intoSuperHScene(info);
                }else if(hSenceService.checkStudyHScence(message)){
                    hSenceService.studyHScence(info);
                }else if(hSenceService.checkDoHScence(message)) {
                    hSenceService.doHScence(info);
                }
//                else if(message.contains("#备忘录")){
//                    Posts.addAll(autoReapeatService.learnConfig(message,group_id,user_id,map,person_last_info.getOrDefault(group_id,null)));
//                }
                else if(autoReapeatService.checkLock(message)) {
                    autoReapeatService.lockRepeat(info);
                }else if(autoReapeatService.checkUnlock(message)) {
                    autoReapeatService.unlockRepeat(info);
                }else if(autoReapeatService.checkLearn(message)) {
                   autoReapeatService.learnRepeat(info);
                }else if(autoReapeatService.checkFastLearn(message)){
                    autoReapeatService.easyLearnRepeat(info);
                }else {
                    autoReapeatService.doRepeat(info);
                }


                String RedisKey = RedisKeyUtil.getLastInfo(group_id,"0");
//                group_last_info.put(group_id,message);
                redisTemplate.opsForList().rightPush(RedisKey,message);
                if(redisTemplate.opsForList().size(RedisKey) == REDIS_LIST_MAX_SIZE) {
                    redisTemplate.opsForList().leftPop(RedisKey);
                }
                RedisKey = RedisKeyUtil.getLastInfo(group_id,user_id);
                redisTemplate.opsForList().rightPush(RedisKey,message);
                if(redisTemplate.opsForList().size(RedisKey) == REDIS_LIST_MAX_SIZE) {
                    redisTemplate.opsForList().leftPop(RedisKey);
                }

            }

        }
    }


    @Override
    public JSONObject getJSONParam(HttpServletRequest request){
        JSONObject jsonParam = null;
        try {
            // 获取输入流
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));

            // 数据写入Stringbuilder
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = streamReader.readLine()) != null) {
                sb.append(line);
            }
            jsonParam = JSONObject.parseObject(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonParam;
    }

    private Map<String,String> getMessageInfo(JSONObject jsonParam) {
        Map<String ,String > info = new HashMap<>();
        info.put("self_id",jsonParam.getString("self_id"));
        info.put("message_type",jsonParam.getString("message_type"));
        info.put("sub_type",jsonParam.getString("sub_type"));
        info.put("message_id",jsonParam.getString("message_id"));
        info.put("user_id",jsonParam.getString("user_id"));
        info.put("message",jsonParam.getString("message"));
        info.put("group_id",jsonParam.getString("group_id"));
        Map<String,Object> sender = JSON.parseObject(jsonParam.getString("sender"));
        String nickname = (String) sender.get("nickname");
        info.put("nickname",nickname);
        return info;
    }

}
