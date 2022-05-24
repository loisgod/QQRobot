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
            System.out.println("decoder:" + s);
        }
        return res;
    }


}
