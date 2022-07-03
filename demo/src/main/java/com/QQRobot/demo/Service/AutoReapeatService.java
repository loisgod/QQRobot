package com.QQRobot.demo.Service;

import com.QQRobot.demo.Utils.*;
import com.QQRobot.demo.dao.ConfigMapper;
import com.QQRobot.demo.dao.StudyDataMapper;
import com.QQRobot.demo.entity.ConfigData;
import com.QQRobot.demo.entity.StudyData;
import com.QQRobot.demo.event.EventProducer;
import com.alibaba.fastjson.JSON;
import com.mysql.cj.util.StringUtils;
import javafx.geometry.Pos;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class AutoReapeatService implements ConstantUtil {

    @Autowired
    private StudyDataMapper studyDataMapper;
    @Autowired
    private ConfigMapper configMapper;
    @Autowired
    private CQService cqService;
    @Autowired
    private RedisTemplate redisTemplate;
    private String Image_Downloard_path = "c:/images/Common";
    private Map<String ,Integer> last_repeat_id = new HashMap<>();
    private String url = "http://localhost:5700/send_msg";
    private String delete_url = "http://localhost:5700/delete_msg";

    private String[] learnRepeatModel = {"#严谨","#魔怔","#学习"};
    private String[] fastLearnRepeatModel = {"#快速严谨","#快速魔怔","#快速学习"};
    private String[] lockModel = {"#口住","#lock"};
    private String[] unlockModel = {"#拔出","#unlock","#啵"};

    public boolean checkLock(@NotNull String message) {
        if (message.length()<3) return false;
        for(String s:lockModel) {
            if(message.contains(s)) return true;
        }
        return false;
    }
    public boolean checkUnlock(@NotNull String message) {
        if (message.length()<2) return false;
        for(String s:unlockModel) {
            if(message.contains(s)) return true;
        }
        return false;
    }
    public boolean checkLearn(@NotNull String message) {
        if (message.length()<3) return false;
        for(String s:learnRepeatModel) {
            if(message.contains(s)) return true;
        }
        return false;
    }
    public boolean checkFastLearn(@NotNull String message) {
        if(message.length()<5) return false;
        for(String s:fastLearnRepeatModel) {
            if(message.contains(s)) return true;
        }
        return false;
    }

    public void learnRepeat(Map<String ,String> info){
        String group_id = info.get("group_id");
        String user_id = info.get("user_id");
        String message = info.get("message");
        int success_count = 0;
        int crazy = -1;
        String m = message.substring(0,3);
        for(String s:learnRepeatModel) {
            ++crazy;
            if(m.equals(s)) break;
        }

        String[] s = message.split(" ");
        if(s.length == 1){
            cqService.sendGroupMessage(info,AUTO_REPEAT_INFO);
            return ;
        } else if (s.length == 2 || message.contains("[CQ:reply")) {
            message = message.replaceAll("#","#快速");
            info.put("message",message);
            easyLearnRepeat(info);
            return;
        }
        s= DataUtil.decoder(learnRepeatModel[crazy],message);
        String[] keyword;
        String[] content;
        if(message.contains("全局")) group_id="";
        keyword=s[0].split("&amp;");
        content=s[1].split("&amp;");

        for(String k:keyword) {
            for (String c : content) {
                if(StringUtils.isEmptyOrWhitespaceOnly(c) || StringUtils.isEmptyOrWhitespaceOnly(k)) continue;
                Map<String,Object> contentMap = DataUtil.allCqDecoder(c);
                if(contentMap.containsKey("images")) {
                    c = CqUtil.CQImageencoder(CqUtil.CQAllDownloardImage((List<String>)contentMap.get("images"),Image_Downloard_path).get(0));
                }
                c = c + (String)contentMap.get("message");
                System.out.println("will insert " + c);
                StudyData studyData = new StudyData(k, c, group_id, user_id, crazy, new Date(),0);
                studyDataMapper.insertStudyData(studyData);
                ++success_count;
            }
        }
        if (success_count!=0) {
            cqService.sendGroupMessage(info,CqUtil.getReply(info.get("message_id")) + (success_count==1?"get☆DAZE~":"get☆DAZE~ * " + success_count));
        }
    }

    public void easyLearnRepeat(Map<String ,String> info) {
        String group_id = info.get("group_id");
        String user_id = info.get("user_id");
        String message = info.get("message");

        int crazy = -1;
        String finalMessageId = "";
        Map<String,Object> map = DataUtil.allCqDecoder(message);
        String m = ((String)map.get("message")).substring(0,5);
        System.out.println(m);
        for(String s:fastLearnRepeatModel) {
            ++crazy;
            if(m.equals(s)) break;
        }
        if(message.contains("全局")) group_id="";
//        String keyword = DataUtil.fastDecoder(fastLearnRepeatModel[crazy],message);
        String keyword = ((String) map.get("message")).replaceAll(fastLearnRepeatModel[crazy],"").replace("全局","").trim();
        if(StringUtils.isEmptyOrWhitespaceOnly(keyword)) {
            cqService.sendGroupMessage(info,FAST_LEARNING_INFO);
            return;
        }
        String RedisKey = RedisKeyUtil.getLastInfo(group_id,user_id);
        String content = (String) redisTemplate.opsForList().index(RedisKey,-1);
        if(map.containsKey("reply")) {
            System.out.println(map);
            String id = (String) map.get("reply");
            finalMessageId = id;
            content = cqService.getGroupMessage(info,id);
        }
        Map<String,Object> contentMap = DataUtil.allCqDecoder(content);
        if(contentMap.containsKey("images")) {
            content = CqUtil.CQImageencoder(CqUtil.CQAllDownloardImage((List<String>)contentMap.get("images"),Image_Downloard_path).get(0));
        }
        content = content + ((String)map.get("message"))
                .replaceAll(fastLearnRepeatModel[crazy],"")
                .replace("全局","")
                .replaceAll(keyword,"")
                .trim();;
        StudyData studyData = new StudyData(keyword, content, group_id, user_id, crazy, new Date(),0);
        studyDataMapper.insertStudyData(studyData);
        cqService.sendGroupMessage(info,CqUtil.getReply(finalMessageId) + "fast get☆DAZE~");

    }

    public void doRepeat(Map<String ,String> info){
        String group_id = info.get("group_id");
        String user_id = info.get("user_id");
        String message = info.get("message");
        ConfigData configData = configMapper.selectConfigDataByKeyword(group_id,message);
        if(configData!=null) {
            cqService.sendGroupMessage(info,configData.getContent());
            return ;
        }
        List<StudyData> list = studyDataMapper.selectByKey(group_id,message,0);
        List<String> crazyData = checkCrazy(message,group_id);
//        for(StudyData s : list) {
//            System.out.println("Reapeat:" + s);
//        }
        if(!list.isEmpty()) {
            int next = new Random().nextInt(list.size());
            StudyData next_object = list.get(next);
            if (list.size()>1 && next_object.getId() == last_repeat_id.getOrDefault(next_object.getKeyword(),-1)) {
                list.remove(next);
                next = new Random().nextInt(list.size());
                next_object = list.get(next);
            }
            cqService.sendGroupMessage(info,next_object.getContent());
            last_repeat_id.put(next_object.getKeyword(),next_object.getId());
        } else if(!crazyData.isEmpty()) {
            for(String s:crazyData) {
                cqService.sendGroupMessage(info,s);
            }
        } else{
            String RedisKey = RedisKeyUtil.getLastInfo(group_id,"0");
            String last_info = (String) redisTemplate.opsForList().index(RedisKey,-1);
            if(last_info!=null && last_info.equals(message)) {
                cqService.sendGroupMessage(info,message);
            }
        }
    }
//    public List<String []> learnConfig(@NotNull String message,String group_id,String user_id,Map<String ,Object> map,Map<String,Object> last_info) {
//        List<String []> Posts = new ArrayList<>();
//
//        String[] s= message.split(" ");
//        String keyword;
//        String content;
//        if(s.length<2){
//            map.put("message",REMEMBER_REPEAT_INFO);
//            Posts.add(new String[]{url, JSON.toJSONString(map)});
//            return Posts;
//        } else if(s.length==2) {
//            if(last_info == null || !last_info.containsKey(user_id)) {
//                map.put("message",EASY_LEARNING_FAIL);
//                Posts.add(new String[]{url,JSON.toJSONString(map)});
//                return Posts;
//            }
//            keyword = message.replace("#备忘录","").trim();
//            content = (String)last_info.get(user_id);
//
//        } else {
//            keyword = s[1];
//            content = s[2];
//        }
//        if(content.contains("CQ:image")) {
//            content = CqUtil.CQImageencoder(CqUtil.CQAllDownloardImage(content,Image_Downloard_path).get(0));
//            System.out.println("encoder "+ content);
//        }
//        if(configMapper.selectConfigDataByKeyword(group_id,keyword)==null) {
//            configMapper.insertConfigData(new ConfigData(keyword,content,group_id));
//        } else {
//            configMapper.updateConfigData(group_id,keyword,content);
//        }
//        map.put("message", "设置DAZE~");
//        Posts.add(new String[]{url, JSON.toJSONString(map)});
//        return Posts;
//
//    }
    public void lockRepeat(Map<String ,String > info) {
        String message = info.get("message");
        String group_id = info.get("group_id");
        int pos = -1;
        for(String s:lockModel) {
            ++pos;
            if(message.contains(s)) break;
        }

        String[] s= DataUtil.decoder(lockModel[pos],message);
        if(s.length<2){
            cqService.sendGroupMessage(info,LOCK_INFO);
            return ;
        }
        String keyword = s[0];
        String content = s[1];
        List<StudyData> list ;
        if(content.equals("*")) {
            list = studyDataMapper.selectByKey(group_id,keyword,0);
        } else {
            list = studyDataMapper.selectByKeyAndContent(group_id,keyword,content,0);
        }
        if(list.size()==0) {
            cqService.sendGroupMessage(info,LOCK_NULL);
            return ;
        }
        for (StudyData studyData:list) {
            studyDataMapper.updateActive(studyData.getId(),1);
        }
        cqService.sendGroupMessage(info,LOCK_SUCCESS);
        return ;
    }
    public void unlockRepeat(Map<String ,String > info) {
        String message = info.get("message");
        String group_id = info.get("group_id");
        int pos = -1;
        for(String s:unlockModel) {
            ++pos;
            if(message.contains(s)) break;
        }

        String[] s= DataUtil.decoder(unlockModel[pos],message);
        if(s.length<2){
            cqService.sendGroupMessage(info,LOCK_INFO);
            return ;
        }
        String keyword = s[0];
        String content = s[1];
        List<StudyData> list;
        if(content.equals("*")) {
            list = studyDataMapper.selectByKey(group_id,keyword,1);
        } else {
            list = studyDataMapper.selectByKeyAndContent(group_id,keyword,content,1);
        }
        if(list.size()==0) {
            cqService.sendGroupMessage(info,UNLOCK_NULL);
            return ;
        }
        for (StudyData studyData:list) {
            studyDataMapper.updateActive(studyData.getId(),0);
        }
        cqService.sendGroupMessage(info,UNLOCK_SUCCESS);
    }
    @NotNull
    private List<String> checkCrazy(String message, String comeFrom) {
        List<StudyData> list = studyDataMapper.selectByCrazy(1,comeFrom,0);
        List<StudyData> list2 = studyDataMapper.selectByCrazy(2,comeFrom,0);
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
            List<StudyData> l = studyDataMapper.selectByKeyAndCrazy(comeFrom,s,2,0);
            if(!l.isEmpty()) {
                int next = new Random().nextInt(l.size());
                StudyData next_object = l.get(next);
                if (l.size()>1 && next_object.getId() == last_repeat_id.getOrDefault(next_object.getKeyword(),-1)) {
                    l.remove(next);
                    next = new Random().nextInt(l.size());
                    next_object = l.get(next);
                }
                res.add(next_object.getContent());
                last_repeat_id.put(next_object.getKeyword(),next_object.getId());

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
            List<StudyData> l = studyDataMapper.selectByKeyAndCrazy(comeFrom,s,1,0);
            if(!l.isEmpty()) {
                int next = new Random().nextInt(l.size());
                StudyData next_object = l.get(next);
                if(l.size()>1 && next_object.getId() == last_repeat_id.getOrDefault(next_object.getKeyword(),-1)) {
                    l.remove(next);
                    next = new Random().nextInt(l.size());
                    next_object = l.get(next);
                }
                res.add(next_object.getContent());
                last_repeat_id.put(next_object.getKeyword(),next_object.getId());
            }
        }


        return res;
    }

    public void Help(Map<String,String > info) {
        cqService.sendGroupMessage(info,HELP_INFO);
    }

    public void deleteInfo(Map<String,String > info) {
        String group_id = info.get("group_id");
        String self_id = info.get("self_id");
        String message = info.get("message");
        String RedisKey = RedisKeyUtil.getLastInfo(group_id,self_id);
        message = message.replaceAll("#烦诶","").replaceAll("#撤回","").trim();
        try {
            int times = message.equals("")?1:Integer.parseInt(message);
            for(;times>0;--times) {
                if(redisTemplate.opsForList().size(RedisKey)>0) {
                    int message_id = (Integer) redisTemplate.opsForList().rightPop(RedisKey);
                    cqService.deleteGroupMessage(info,Integer.toString(message_id));
                } else {
                    break;
                }
            }
        } catch (NumberFormatException e) {

        }
    }

    public void speak(Map<String ,String> info) {
        String message = info.get("message");
        message = message.replaceAll("#说话","").replaceAll("\\p{Punct}","").replaceAll(" ","").trim();
        cqService.sendGroupMessage(info,CqUtil.getTTS(message));
    }

}
