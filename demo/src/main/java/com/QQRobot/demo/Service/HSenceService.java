package com.QQRobot.demo.Service;

import com.QQRobot.demo.Utils.ConstantUtil;
import com.QQRobot.demo.Utils.CqUtil;
import com.QQRobot.demo.dao.HScenceDataMapper;
import com.QQRobot.demo.entity.HScenceData;
import com.alibaba.fastjson.JSON;
import javafx.geometry.Pos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class HSenceService implements ConstantUtil {
    private final String[] doHScenceModel={"#瑟图","#涩图","#来张瑟图","#来张涩图"};
    private final String[] studyHScenceModel={"#添加涩图","#添加瑟图"};
    private final String[] superHScenceModel={"#瑟图鸿儒","#涩图鸿儒"};
    private String url = "http://localhost:5700/send_msg";
    private String path = "c:/images/HScence";
    @Autowired
    HScenceDataMapper hScenceDataMapper;

    public boolean checkDoHScence(String message) {
        for(String s : doHScenceModel) {
            if(message.contains(s)) {
                return true;
            }
        }
        return false;
    }
    public boolean checkStudyHScence(String message) {
        for(String s : studyHScenceModel) {
            if(message.contains(s)) {
                return true;
            }
        }
        return false;
    }
    public boolean checkSuperHScence(String message) {
        for(String s : superHScenceModel) {
            if(message.equals(s)) {
                return true;
            }
        }
        return false;
    }

    public List<String []> doHScence(String message, Map<String ,Object> map) {
        List<String []> Posts = new ArrayList<>();
        String[] s = message.split(" ");
        int n;
        try{
            n = s.length>1?Integer.parseInt(s[1]):1;
        } catch (NumberFormatException e) {
            n = 1;
        }
        List<HScenceData> list = hScenceDataMapper.selectHScence(n);
        List<String > CQ = CqUtil.CQImageencoders(list);
        for(String string:CQ) {
            System.out.println("CQ" + string);
            map.put("message",string);
            Posts.add(new String[]{url,JSON.toJSONString(map)});
        }
        return Posts;
    }
    public List<String []> studyHScence(String message, Map<String,Object> map) {
        List<String []> Posts = new ArrayList<>();
        if(message.equals("#添加瑟图") || message.equals("#添加涩图")) {
            map.put("message",HSCENCE_ADD_INFO);
            Posts.add(new String[]{url,JSON.toJSONString(map)});
            return Posts;
        }
        List<String> finalPath = CqUtil.CQAllDownloardImage(message,path);
        int sucess_count = 0;
        for (String p:finalPath) {
            hScenceDataMapper.insertSHScenceData(new HScenceData(p));
            ++sucess_count;
        }
        if(sucess_count>0) {
            map.put("message", sucess_count==1?"瑟图 get☆DAZE~":"get☆DAZE~ * " + sucess_count);
            Posts.add(new String[]{url, JSON.toJSONString(map)});
        }
        return Posts;
    }
    public List<String []> superHScence(String message, Map<String,Object> map) {
        List<String []> Posts = new ArrayList<>();
        int sucess_count = 0;

        if(message.contains("#")) {
            map.put("message",HSCENCE_SUPER_ERROR_INFO);
            Posts.add(new String[]{url, JSON.toJSONString(map)});
        }
        List<String> finalPath = CqUtil.CQAllDownloardImage(message,path);
        for (String p:finalPath) {
            hScenceDataMapper.insertSHScenceData(new HScenceData(p));
            ++sucess_count;
        }
        if(sucess_count>0) {
            map.put("message", sucess_count==1?"瑟图 get☆DAZE~":"get☆DAZE~ * " + sucess_count);
            Posts.add(new String[]{url, JSON.toJSONString(map)});
        }
        return Posts;
    }
    public List<String []> intoSuperHScene(Map<String,Object> map) {
        List<String []> Posts = new ArrayList<>();
        map.put("message",HSCENCE_SUPER_INFO);
        Posts.add(new String[]{url,JSON.toJSONString(map)});

        return Posts;
    }
    public List<String []> outSuperHScene(Map<String,Object> map) {
        List<String []> Posts = new ArrayList<>();
        map.put("message",HSCENCE_SUPER_OUT);
        Posts.add(new String[]{url,JSON.toJSONString(map)});

        return Posts;
    }

    public List<String []> HScenceCounter(Map<String,Object> map) {
        List<String []> Posts = new ArrayList<>();
        map.put("message","当前库中的瑟图数量为：" + hScenceDataMapper.HScenceCounter());
        Posts.add(new String[]{url,JSON.toJSONString(map)});

        return Posts;
    }

}
