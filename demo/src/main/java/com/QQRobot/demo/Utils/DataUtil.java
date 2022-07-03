package com.QQRobot.demo.Utils;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtil {
    public static String[] decoder(String key,String message) {
        System.out.println(message);
        String studyMatch = key + ".*?\\s+(.*?)\\s+(.*)\\s?";
        System.out.println(studyMatch);
        Pattern pattern = Pattern.compile(studyMatch);
        Matcher matcher = pattern.matcher(message);
        String[] res = {};
        while(matcher.find()) {
//            for (int i=0;i<=matcher.groupCount();++i) {
//                System.out.println(String.format("group(%d)", i) + matcher.group(i));
//            }
            res = new String[]{matcher.group(1),matcher.group(2)};
            break;
        }
        for(String s:res) {
            System.out.println("learn decoder:" + s);
        }
        return res;
    }
    public static String fastDecoder(String key,String message) {
        System.out.println(message);
        String studyMatch = key + ".*?\\s+(.*?)\\s?";
        System.out.println(studyMatch);
        Pattern pattern = Pattern.compile(studyMatch);
        Matcher matcher = pattern.matcher(message);
        String res = "";
        if(matcher.find()) {
//            for (int i=0;i<=matcher.groupCount();++i) {
//                System.out.println(String.format("group(%d)", i) + matcher.group(i));
//            }
            res = matcher.group(1);
        }
        System.out.println("fast decoder:" + res);
        return res;
    }

    private static String replyDecoder(String message) {
        System.out.println(message);
        String replyMatch = ".*?\\[CQ:reply,id=(.*?)\\].*?";
        Pattern pattern = Pattern.compile(replyMatch);
        Matcher matcher = pattern.matcher(message);
        String res = "";
        if(matcher.find()) {
//            for (int i=0;i<=matcher.groupCount();++i) {
//                System.out.println(String.format("group(%d)", i) + matcher.group(i));
//            }
            res = matcher.group(1);
        }
        System.out.println("reply decoder:" + res);
        return res;
    }

    private static String atDecoder(String message) {
        System.out.println(message);
        String atMatch = ".*?\\[CQ:at,qq=(.*?)\\].*?";
        Pattern pattern = Pattern.compile(atMatch);
        Matcher matcher = pattern.matcher(message);
        String res = "";
        if(matcher.find()) {
//            for (int i=0;i<=matcher.groupCount();++i) {
//                System.out.println(String.format("group(%d)", i) + matcher.group(i));
//            }
            res = matcher.group(1);
        }
        System.out.println("at decoder:" + res);
        return res;
    }

    private static List<String> imageDecoder(String message) {
        System.out.println(message);
        System.out.println("image before: " + message);
        String imageMatch = ".*?\\[CQ:image,file=(.*?),url=(.*?)\\].*?";
        List<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile(imageMatch);
        Matcher matcher = pattern.matcher(message);
        while(matcher.find()) {
            list.add(matcher.group(2));
            System.out.println(matcher.group(2));
        }
        return list;
    }

    public static Map<String,Object> allCqDecoder(String message) {
        Map<String,Object> map = new HashMap<>();
        String reply_id = replyDecoder(message);
        if(!"".equals(reply_id)) {
            map.put("reply",reply_id);
        }
        message = message.replaceAll("\\[CQ:reply.*\\]","");
        List<String> images = imageDecoder(message);
        if(!images.isEmpty()) {
            map.put("images",images);
        }
        message = message.replaceAll("\\[CQ:image.*?\\]","");
        String at_id = atDecoder(message);
        if(!"".equals(at_id)) {
            map.put("at",at_id);
        }
        message = message.replaceAll("\\[CQ:at.*\\]","");
        map.put("message",message.trim());
        return map;
    }


}
