package com.QQRobot.demo.dao;

import com.QQRobot.demo.entity.StudyData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface StudyDataMapper {

    @Insert({
            "insert into study(keyword,content,come_from,teacher,create_time,crazy) ",
            "values(#{keyword},#{content},#{comeFrom},#{teacher},#{createTime},#{crazy})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertStudyData(StudyData studyData);

//    StudyData seletById(int id);
//
//    List<StudyData> selectByFrom(String from);
//
@Select({
        "select id,keyword,content,come_from,teacher,create_time,crazy",
        "from study where (come_from=#{comeFrom} OR come_from=\"\") and keyword=#{keyword}"
})
    List<StudyData> selectByKey(String comeFrom,String keyword);

    @Select({
            "select distinct keyword,come_from,crazy",
            "from study where (come_from=#{comeFrom} OR come_from=\"\") and crazy=#{crazy}"
    })
    List<StudyData> selectByCrazy(int crazy,String comeFrom);

    @Select({
            "select id,keyword,content,come_from,teacher,create_time,crazy",
            "from study where (come_from=#{comeFrom} OR come_from=\"\") and keyword=#{keyword} and crazy=#{crazy}"
    })
    List<StudyData> selectByKeyAndCrazy(String comeFrom,String keyword,int crazy);
//
//    List<StudyData> selectByTeacher(String from,String teacher);


}
