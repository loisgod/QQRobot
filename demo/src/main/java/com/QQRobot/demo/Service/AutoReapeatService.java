package com.QQRobot.demo.Service;

import com.QQRobot.demo.Utils.ConstantUtil;
import com.QQRobot.demo.Utils.CqUtil;
import com.QQRobot.demo.Utils.DataUtil;
import com.QQRobot.demo.dao.RememberDataMapper;
import com.QQRobot.demo.dao.StudyDataMapper;
import com.QQRobot.demo.entity.RememberData;
import com.QQRobot.demo.entity.StudyData;
import com.alibaba.fastjson.JSON;
import com.mysql.cj.util.StringUtils;
import javafx.geometry.Pos;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class AutoReapeatService implements ConstantUtil {

    @Autowired
    private StudyDataMapper studyDataMapper;
    @Autowired
    private RememberDataMapper rememberDataMapper;
    private String path = "c:/images/Common";
    private String url = "http://localhost:5700/send_msg";
    private String[] learnRepeatModel = {"#严谨","#魔怔","#学习"};
    private String[] fastLearnRepeatModel = {"#快速严谨","#快速魔怔","#快速学习"};

    public boolean checkLearn(@NotNull String message) {
        if (message.length()<3) return false;
        String m = message.substring(0,3);
        for(String s:learnRepeatModel) {
            if(m.equals(s)) return true;
        }
        return false;
    }
    public boolean checkFastLearn(@NotNull String message) {
        if(message.length()<5) return false;
        String m = message.substring(0,5);
        for(String s:fastLearnRepeatModel) {
            if(m.equals(s)) return true;
        }
        return false;
    }

    public List<String []> learnRepeat(@NotNull String message, String group_id, String user_id, Map<String ,Object> map){
        int success_count = 0;
        int crazy = -1;
        List<String []> Posts = new ArrayList<>();
        String m = message.substring(0,3);
        for(String s:learnRepeatModel) {
            ++crazy;
            if(m.equals(s)) break;
        }

        String[] s= DataUtil.decoder(learnRepeatModel[crazy],message);
        String[] keyword;
        String[] content;
        if(s.length<2){
            map.put("message",AUTO_REPEAT_INFO);
            Posts.add(new String[]{url, JSON.toJSONString(map)});
            return Posts;
        }
        if(message.contains("全局")) group_id="";
        keyword=s[0].split("&amp;");
        content=s[1].split("&amp;");

        for(String k:keyword) {
            for (String c : content) {
                if(StringUtils.isEmptyOrWhitespaceOnly(c) || StringUtils.isEmptyOrWhitespaceOnly(k)) continue;
                if(c.contains("CQ:image")) {
                    c = CqUtil.CQImageencoder(CqUtil.CQAllDownloardImage(c,path).get(0));
                    System.out.println("encoder "+ c);
                }
                StudyData studyData = new StudyData(k, c, group_id, user_id, crazy, new Date());
                studyDataMapper.insertStudyData(studyData);
                ++success_count;
            }
        }
        if (success_count!=0) {
            map.put("message", success_count==1?"get☆DAZE~":"get☆DAZE~ * " + success_count);
            Posts.add(new String[]{url, JSON.toJSONString(map)});
        }
        return Posts;
    }

    public List<String []> easyLearnRepeat(String message,String group_id,String user_id,Map<String,Object> map,Map<String,Object> last_info) {
        List<String []> Posts = new ArrayList<>();
        if(last_info == null || !last_info.containsKey(user_id)) {
            map.put("message",EASY_LEARNING_FAIL);
            Posts.add(new String[]{url,JSON.toJSONString(map)});
            return Posts;
        }
        int crazy = -1;
        String m = message.substring(0,5);
        for(String s:fastLearnRepeatModel) {
            ++crazy;
            if(m.equals(s)) break;
        }
        if(message.contains("全局")) group_id="";
        String keyword = message.replace(fastLearnRepeatModel[crazy],"").trim();
        if(StringUtils.isEmptyOrWhitespaceOnly(keyword)) {
            map.put("message",FAST_LEARNING_INFO);
            Posts.add(new String[]{url,JSON.toJSONString(map)});
            return Posts;
        }
        String content = (String)last_info.get(user_id);

        if(content.contains("CQ:image")) {
            content = CqUtil.CQImageencoder(CqUtil.CQAllDownloardImage(content,path).get(0));
            System.out.println("encoder "+ content);
        }
        StudyData studyData = new StudyData(keyword, content, group_id, user_id, crazy, new Date());
        studyDataMapper.insertStudyData(studyData);
        map.put("message", "fast get☆DAZE~");
        Posts.add(new String[]{url, JSON.toJSONString(map)});
        return Posts;

    }

    public List<String []> doRepeat(String message,String group_id,String user_id,Map<String ,Object> map,Map<String ,Object> last_info){
        List<String []> Posts = new ArrayList<>();
        RememberData rememberData = rememberDataMapper.selectRemember(group_id,message);
        if(rememberData!=null) {
            map.put("message",rememberData.getContent());
            Posts.add(new String[]{url,JSON.toJSONString(map)});
            return Posts;
        }
        List<StudyData> list = studyDataMapper.selectByKey(group_id,message);
        List<String> crazyData = checkCrazy(message,group_id);
//        for(StudyData s : list) {
//            System.out.println("Reapeat:" + s);
//        }
        if(!list.isEmpty()) {
            map.put("message",list.get(new Random().nextInt(list.size())).getContent());
            Posts.add(new String[]{url,JSON.toJSONString(map)});
        } else if(!crazyData.isEmpty()) {
            for(String s:crazyData) {
                map.put("message",s);
                Posts.add(new String[]{url,JSON.toJSONString(map)});
            }
        } else if( (last_info.containsKey(group_id)?last_info.get(group_id):"").equals(message)) {
            map.put("group_id",group_id);
            map.put("message",message);
            Posts.add(new String[]{url,JSON.toJSONString(map)});
        }

        return Posts;
    }
    public List<String []> learnRemember(@NotNull String message,String group_id,String user_id,Map<String ,Object> map,Map<String,Object> last_info) {
        List<String []> Posts = new ArrayList<>();

        String[] s= message.split(" ");
        String keyword;
        String content;
        if(s.length<2){
            map.put("message",REMEMBER_REPEAT_INFO);
            Posts.add(new String[]{url, JSON.toJSONString(map)});
            return Posts;
        } else if(s.length==2) {
            if(last_info == null || !last_info.containsKey(user_id)) {
                map.put("message",EASY_LEARNING_FAIL);
                Posts.add(new String[]{url,JSON.toJSONString(map)});
                return Posts;
            }
            keyword = message.replace("#备忘录","").trim();
            content = (String)last_info.get(user_id);

        } else {
            keyword = s[1];
            content = s[2];
        }
        if(content.contains("CQ:image")) {
            content = CqUtil.CQImageencoder(CqUtil.CQAllDownloardImage(content,path).get(0));
            System.out.println("encoder "+ content);
        }
        if(rememberDataMapper.selectRemember(group_id,keyword)==null) {
            rememberDataMapper.insertRememberData(new RememberData(keyword,content,group_id,user_id));
        } else {
            rememberDataMapper.updateRemember(group_id,keyword,content);
        }
        map.put("message", "记下来咯~");
        Posts.add(new String[]{url, JSON.toJSONString(map)});
        return Posts;

    }

    @NotNull
    private List<String> checkCrazy(String message, String comeFrom) {
        List<StudyData> list = studyDataMapper.selectByCrazy(1,comeFrom);
        List<StudyData> list2 = studyDataMapper.selectByCrazy(2,comeFrom);
        List<String> res = new ArrayList<>();
        if(list==null && list2 == null) {
            return res;
        }
//        for(StudyData l:list) {
//            System.out.println("魔怔表：" + l);
//        }
//        for(StudyData l:list2) {
//            System.out.println("补风表：" + l);
//        }

        Set<String> set = new HashSet<>(); //  先补风
        for(StudyData studyData:list2) {
            if(message.contains(studyData.getKeyword())) {
                set.add(studyData.getKeyword());
            }
        }
        for (String s:set) {
            List<StudyData> l = studyDataMapper.selectByKeyAndCrazy(comeFrom,s,2);
            if(!l.isEmpty()) {
                res.add(l.get(new Random().nextInt(l.size())).getContent());
            }
        }
        if(!res.isEmpty()) {  // 捕风捉影则不魔怔
            return res;
        }


        set = new HashSet<>();
        for(StudyData studyData:list) {
            if(CqUtil.Mathching(studyData.getKeyword(),message)) {
                set.add(studyData.getKeyword());
            }
        }
        for (String s:set) {
            List<StudyData> l = studyDataMapper.selectByKeyAndCrazy(comeFrom,s,1);
            if(!l.isEmpty()) {
                res.add(l.get(new Random().nextInt(l.size())).getContent());
            }
        }


        return res;
    }

    public List<String []> Help(@NotNull Map<String,Object> map) {
        List<String []> Posts = new ArrayList<>();
        map.put("message",HELP_INFO);
        Posts.add(new String[]{url,JSON.toJSONString(map)});

        return Posts;
    }
}
