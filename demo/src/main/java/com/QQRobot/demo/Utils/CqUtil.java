package com.QQRobot.demo.Utils;

import com.QQRobot.demo.entity.HScenceData;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CqUtil {

    public static boolean Mathching(String patterner,String content) {
        String[] patterners = patterner.split("");
        String pattern = ".*";
        for(String s: patterners) {
            pattern = pattern + s + ".*";
        }
        return Pattern.matches(pattern,content);
    }
    static final String CQImagepattern = ".*?\\[CQ:image,file=(.*?),url=(.*?)\\].*?";
    static final Pattern cqip = Pattern.compile(CQImagepattern);
    public static List<String> CQImageencoders(List<HScenceData> list) {
        List<String> result = new ArrayList<>();
        for(HScenceData hScenceData:list) {
            result.add(CQImageencoder(hScenceData.getFileUrl()));
        }
        return result;
    }
    public static String CQImageencoder(String path) {
        String file = "file:///" + path;
        file = String.format("[CQ:%s,file=%s,url=%s]", "image",file,file);
        return file;
    }
    public static List<String> CQImagedecoder(String CQ) {
        System.out.println(CQ);
        System.out.println(Pattern.matches(CQImagepattern,CQ));
        Matcher cqim = cqip.matcher(CQ);
        List<String> list = new ArrayList<>();
        while(cqim.find()) {
            list.add(cqim.group(2));
            System.out.println("group(2) = " + cqim.group(2));
        }
        return list;
    }
    public static String CqDownloardImages(String urlList,String path) {
        path = path + "/" + new Date().getTime() + ".gif";
        try {
            URL url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while((length = dataInputStream.read(buffer))>0) {
                output.write(buffer,0,length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return path;
    }

    public static List<String> CQAllDownloardImage(String message, String path)  {
        List<String> allUrl = CQImagedecoder(message);
        List<String> allPath = new ArrayList<>();
        for(String s:allUrl) {
            allPath.add(CqDownloardImages(s,path));
        }
        return allPath;
    }
}
