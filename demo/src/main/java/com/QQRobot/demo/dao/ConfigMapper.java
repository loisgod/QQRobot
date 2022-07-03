package com.QQRobot.demo.dao;

import com.QQRobot.demo.entity.ConfigData;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConfigMapper {

    int insertConfigData(ConfigData configData);

    ConfigData selectConfigDataByKeyword(String comeFrom, String keyword);

    int updateConfigData(String comeFrom,String keyword,String content);



}
