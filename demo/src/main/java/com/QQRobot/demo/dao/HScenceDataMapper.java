package com.QQRobot.demo.dao;

import com.QQRobot.demo.entity.HScenceData;
import com.QQRobot.demo.entity.StudyData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HScenceDataMapper {
    @Insert({
            "insert into hscence(file_url) ",
            "values(#{fileUrl})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertSHScenceData(HScenceData hScenceData);

    @Select({
            "select id,file_url",
            "from hscence order by rand() limit #{n} "
    })
    List<HScenceData> selectHScence(int n);

    @Select({
            "select count(id)" ,
            "from hscence "
    })
    int HScenceCounter();
}
