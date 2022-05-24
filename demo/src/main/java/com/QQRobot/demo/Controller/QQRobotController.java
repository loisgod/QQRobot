package com.QQRobot.demo.Controller;

import com.QQRobot.demo.Service.AutoReapeatService;
import com.QQRobot.demo.Service.QQRobotService;
import com.QQRobot.demo.Service.QQRobotServicelmpl;
import com.QQRobot.demo.Utils.HttpRequestUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class QQRobotController {

    @Resource
    private QQRobotService robotService;


    @PostMapping
    public void QqRobotEven(HttpServletRequest request){
        System.out.println("POST" + request);
        robotService.QQRobotEvenHandle(request);
    }
    @GetMapping
    @ResponseBody
    public String  GetQqRobotEven(HttpServletRequest request){
        Map<String, Object > map =new HashMap<>();
        map.put("message_type","group");
        map.put("group_id",123);
        map.put("message","喂喂喂，歪比巴布");
        return JSON.toJSONString(map);
    }

    @RequestMapping(path = "/demo")
    @ResponseBody
    public String http(HttpServletRequest request, HttpServletResponse response){
//        QQRobotServicelmpl qqRobotServicelmpl = new QQRobotServicelmpl();
//        qqRobotServicelmpl.QQRobotEvenHandle(request);

        return "Test";
    }


}
