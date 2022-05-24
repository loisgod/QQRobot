package com.QQRobot.demo.Service;

import com.QQRobot.demo.Utils.ConstantUtil;
import com.QQRobot.demo.Utils.DataUtil;
import com.QQRobot.demo.Utils.HttpRequestUtil;
import com.QQRobot.demo.dao.StudyDataMapper;
import com.QQRobot.demo.entity.StudyData;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;
import javafx.geometry.Pos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private Map<String, Object> group_last_info = new HashMap<>();
    Map<String ,Map<String ,Object>> person_last_info = new HashMap<>();
    Map<String ,Object> temp ;
    private Map<String ,Object> user_status = new HashMap<>();
    @Autowired
    private AutoReapeatService autoReapeatService;
    @Autowired
    private HSenceService hSenceService;



    @Override
    public void QQRobotEvenHandle(HttpServletRequest request) {
        //JSONObject
        JSONObject jsonParam = this.getJSONParam(request);
        log.info("接收参数为:{}",jsonParam.toString() !=null ? "SUCCESS:" + jsonParam.toString() : "FALSE");
        if("message".equals(jsonParam.getString("post_type"))){
            String message_type  = jsonParam.getString("message_type");


            if("group".equals(message_type)) {
                List<String []> Posts = new ArrayList<>();
                String message = jsonParam.getString("message").replaceAll("\\s+"," ").trim();
                System.out.println("Message  " + JSON.toJSONString(message));
                String group_id = jsonParam.getString("group_id");
                Map<String,Object> sender = JSON.parseObject(jsonParam.getString("sender"));
                String user_id = Integer.toString((int)sender.get("user_id"));

                Map<String, Object > map =new HashMap<>();
                map.put("message_type","group");
                map.put("group_id",group_id);


                if(user_status.getOrDefault(user_id,null)!=null){
                    switch ((int)user_status.get(user_id)) {
                        case HSCENCE_INSERT:if(message.contains("#贤者模式")) {
                            user_status.remove(user_id);
                            Posts.addAll(hSenceService.outSuperHScene(map));
                        } else {
                            Posts.addAll(hSenceService.superHScence(message,map));
                        }break;
                        default:break;
                    }
                }else if(message.equals("#瑟图总数")){
                    Posts.addAll(hSenceService.HScenceCounter(map));
                }else if(message.equals("#帮助")){
                    Posts.addAll(autoReapeatService.Help(map));
                }else if(hSenceService.checkSuperHScence(message)) {
                    user_status.put(user_id,HSCENCE_INSERT);
                    Posts.addAll(hSenceService.intoSuperHScene(map));
                }else if(hSenceService.checkStudyHScence(message)){
                    Posts.addAll(hSenceService.studyHScence(message,map));
                }else if(hSenceService.checkDoHScence(message)) {
                    Posts.addAll(hSenceService.doHScence(message,map));
                }else if(message.contains("#备忘录")){
                    Posts.addAll(autoReapeatService.learnRemember(message,group_id,user_id,map,person_last_info.getOrDefault(group_id,null)));
                }else if(autoReapeatService.checkLearn(message)) {
                   Posts.addAll(autoReapeatService.learnRepeat(message,group_id,user_id,map));
                }else if(autoReapeatService.checkFastLearn(message)){
                    Posts.addAll(autoReapeatService.easyLearnRepeat(message,group_id,user_id,map,person_last_info.getOrDefault(group_id,null)));
                }else {
                    Posts.addAll(autoReapeatService.doRepeat(message,group_id,user_id,map,group_last_info));
                }



                group_last_info.put(group_id,message);
                if(!person_last_info.containsKey(group_id)) {
                    person_last_info.put(group_id,new HashMap<>());
                }
                temp = person_last_info.get(group_id);
                temp.put(user_id,message);
                List<String > result=HttpRequestUtil.doPosts(Posts);
                if(!result.isEmpty()) {
                    log.info("发送成功");
                    for(String s:result) {
                        log.info(s);
                    }

                }
            }

        }
    }



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

}
