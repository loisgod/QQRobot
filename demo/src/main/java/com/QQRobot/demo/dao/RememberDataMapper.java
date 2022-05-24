package com.QQRobot.demo.dao;

import com.QQRobot.demo.entity.RememberData;
import com.QQRobot.demo.entity.StudyData;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RememberDataMapper {
    @Insert({
            "insert into remember(keyword,content,come_from,user_id,create_time) ",
            "values(#{keyword},#{content},#{comeFrom},#{userId},#{createTime})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertRememberData(RememberData rememberData);

    @Select({
            "select id,keyword,content,come_from,user_id,create_time",
            "from remember where come_from=#{comeFrom} and keyword=#{keyword}"
    })
    RememberData selectRemember(String comeFrom, String keyword);

    @Update({
            "update remember set content=#{content} where come_from=#{comeFrom} and keyword=#{keyword}",
    })
    int updateRemember(String comeFrom,String keyword,String content);

}
