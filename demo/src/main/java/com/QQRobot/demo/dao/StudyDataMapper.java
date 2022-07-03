package com.QQRobot.demo.dao;

import com.QQRobot.demo.entity.StudyData;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface StudyDataMapper {

    @Insert({
            "insert into study(keyword,content,come_from,teacher,create_time,crazy,active) ",
            "values(#{keyword},#{content},#{comeFrom},#{teacher},#{createTime},#{crazy},#{active})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertStudyData(StudyData studyData);

//    StudyData seletById(int id);
//
//    List<StudyData> selectByFrom(String from);
//
@Select({
        "select id,keyword,content,come_from,teacher,create_time,crazy,active",
        "from study where (come_from=#{comeFrom} OR come_from=\"\") and keyword=#{keyword} and active=#{active}"
})
    List<StudyData> selectByKey(String comeFrom,String keyword,int active);

    @Select({
            "select distinct keyword,come_from,crazy,active",
            "from study where (come_from=#{comeFrom} OR come_from=\"\") and crazy=#{crazy} and active=#{active}"
    })
    List<StudyData> selectByCrazy(int crazy,String comeFrom,int active);

    @Select({
            "select id,keyword,content,come_from,teacher,create_time,crazy,active",
            "from study where (come_from=#{comeFrom} OR come_from=\"\") and keyword=#{keyword} and crazy=#{crazy} and active=#{active}"
    })
    List<StudyData> selectByKeyAndCrazy(String comeFrom,String keyword,int crazy,int active);
//
//    List<StudyData> selectByTeacher(String from,String teacher);
    @Select({
            "select id,keyword,content,come_from,teacher,create_time,crazy,active",
            "from study where (come_from=#{comeFrom} OR come_from=\"\") and keyword=#{keyword} and content=#{content} and active=#{active}"
    })
    List<StudyData> selectByKeyAndContent(String comeFrom,String keyword,String content,int active);
    @Update({
            "update study set active=#{active} " +
             "where id=#{id}",
    })
    int updateActive(int id,int active);

}
