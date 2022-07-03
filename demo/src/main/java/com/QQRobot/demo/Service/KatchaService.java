package com.QQRobot.demo.Service;

import com.QQRobot.demo.Utils.ConstantUtil;
import com.QQRobot.demo.Utils.KatchaSystem;
import com.QQRobot.demo.Utils.RedisKeyUtil;
import com.QQRobot.demo.entity.ConfigData;
import com.QQRobot.demo.entity.StudyData;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.naming.ldap.Rdn;
import java.util.*;

@Service
@Slf4j
public class KatchaService implements ConstantUtil {
    private String url = "http://localhost:5700/send_msg";
   Map<String ,KatchaSystem> katcha_map = new HashMap<>();
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CQService cqService;
    private final String DEFAULT_GAME = "test";
    private final String DEFAULT_POOL = "test";
    String game = "";
    String pool = "";
    String historyKey = "";
    String historySSRKEY = "";
    String historyUPSSRKEY = "" ;
    String historySRKEY = "";
    int pos = 0;
    int last_UP_SSR = 0;
    int last_SR = 0;
    int last_SSR = 0;

//    public List<String []> katcha(String message, String group_id, String nickname,String user_id, Map<String ,Object> map,int times,boolean simple){
    public boolean KatchaSystem(Map<String,String > info) {
        String  message = info.get("message");
        if(message.equals("#抽卡")){
            katcha(info,1,false);
            return true;
        } else if(message.equals("#十连抽")){
            katcha(info,10,false);
            return true;
        } else if(message.equals("#百连抽")){
            katcha(info,100,false);
            return true;
        } else if (message.equals("#我的卡池")){
            getMyKatchaInfo(info);
            return true;
        } else if (message.equals("#转生")) {
            resetKatcha(info);
            return true;
        } else if(message.contains("#添加") && message.contains("星")) {
            addCard(info);
            return true;
        } else if(message.contains("#删除") && message.contains("星")) {
            deleteCard(info);
            return true;
        } else if(message.equals("#卡池详情")) {
            showCard(info);
            return true;
        }
        return false;
    }
    private void katcha(Map<String,String > info,int times,boolean simple){
        List<String []> Posts = new ArrayList<>();
        String group_id = info.get("group_id");
        String user_id = info.get("user_id");
        String message = info.get("message");
        String nickname = info.get("nickname");
        this.updateConstant(group_id,user_id);
//        map.put("message",katchaSystem.getMulKatcha(user_id,10));
        List<String > list = getKatcha(user_id,times);
        if(simple) {
            getMyKatchaInfo(info);
            return;
        } else {
            String res = "";
            for(String s:list) {
                res = res + " " + s;
            }
            res += "已垫" + last_SSR + "发";

            cqService.sendGroupMessage(info,"来自 " + nickname + res);
        }
    }

    private void getMyKatchaInfo(Map<String,String > info){
        String group_id = info.get("group_id");
        String user_id = info.get("user_id");
        String nickname = info.get("nickname");
        updateConstant(group_id,user_id);
        List<String > history = redisTemplate.opsForList().range(historyKey,0,-1);
        List<Integer> SSR_history = redisTemplate.opsForList().range(historySSRKEY,0,-1);
        List<Integer> UP_SSR_history = redisTemplate.opsForList().range(historyUPSSRKEY,0,-1);
        List<Integer> SR_history = redisTemplate.opsForList().range(historySRKEY,0,-1);
        cqService.sendGroupMessage(info,"来自 " + nickname +"\n" + String.format("在%s游戏的%s池子中\n",game,pool) + getInfo(SSR_history,UP_SSR_history,SR_history,history));

    }

    private String getInfo(List<Integer> SSR_history, List<Integer> UP_SSR_history,List<Integer > SR_history,List<String> history) {
        String global_info = String.format("共抽取%d抽\n共获得%d个五星\n其中%d个UP五星\n共获得%d个四星\n五星出货率%f%%\n五星歪率%f%%\n",history.size(),
                SSR_history.size(),UP_SSR_history.size(),SR_history.size(),(float)SSR_history.size()*100/(float)history.size(),
                (float)(SSR_history.size()-UP_SSR_history.size())*100/(float)SSR_history.size());
        String detail = "出货详情(第几发出货)：";
        int j=0;
        if(!SSR_history.isEmpty()) {
            detail = detail + String.format("%s(%d)",history.get(SSR_history.get(0)-1),SSR_history.get(0));
            if (!UP_SSR_history.isEmpty() && UP_SSR_history.get(0).equals(SSR_history.get(0))) {
                ++j;
            } else {
                detail = detail + "(歪)";
            }
        }
        for(int i=1;i<SSR_history.size();++i) {
            detail = detail + String.format(" %s(%d)",history.get(SSR_history.get(i)-1),SSR_history.get(i) - SSR_history.get(i-1));
            if(j<UP_SSR_history.size()) {
                if(UP_SSR_history.get(j).equals(SSR_history.get(i))) {
                    ++j;
                } else {
                    detail=detail+"(歪)";
                }
            }
        }
        String averge = "\n";
        int SSR_count = SSR_history.isEmpty()?0:SSR_history.get(0);
        for(int i=1;i<SSR_history.size();++i) {
            SSR_count += SSR_history.get(i) - SSR_history.get(i-1);
        }
        int UP_SSR_count = UP_SSR_history.isEmpty()?0:UP_SSR_history.get(0);
        for(int i=1;i<UP_SSR_history.size();++i) {
            UP_SSR_count += UP_SSR_history.get(i) - UP_SSR_history.get(i-1);
        }
        averge = averge + String.format("平均%d发出五星，平均%d发出UP五星",SSR_history.size()==0?-1:SSR_count/SSR_history.size(),UP_SSR_history.size()==0?-1:UP_SSR_count/UP_SSR_history.size()) ;
        return global_info + detail + averge;
    }

    private void resetKatcha(Map<String ,String> info){
        String group_id = info.get("group_id");
        String user_id = info.get("user_id");
        this.updateConstant(group_id,user_id);
        String userRedisKey = RedisKeyUtil.getKatchaUser(game,pool,user_id);
        redisTemplate.delete(userRedisKey);
        redisTemplate.delete(this.historyKey);
        redisTemplate.delete(this.historySSRKEY);
        redisTemplate.delete(this.historyUPSSRKEY);
        redisTemplate.delete(this.historySRKEY);
        cqService.sendGroupMessage(info,KATCHA_RESET);

    }

    private void addCard(Map<String ,String> info){
        String group_id = info.get("group_id");
        String user_id = info.get("user_id");
        String message = info.get("message");
        this.updateConstant(group_id,user_id);
        String[] s = message.split(" ");
        if(s.length == 1) return ;
        String redisKey = getCardDetail(s[0]);
        for (int i = 1; i < s.length; i++) {
            redisTemplate.opsForSet().add(redisKey,s[i]);
        }
        cqService.sendGroupMessage(info,KATCHA_ADD);
    }

    private void deleteCard(Map<String ,String> info){
        String group_id = info.get("group_id");
        String user_id = info.get("user_id");
        String message = info.get("message");
        this.updateConstant(group_id,user_id);
        String[] s = message.split(" ");
        if(s.length == 1) return ;
        String redisKey = getCardDetail(s[0]);
        for (int i = 1; i < s.length; i++) {
            redisTemplate.opsForSet().remove(redisKey,s[i]);
        }
        cqService.sendGroupMessage(info,KATCHA_REMOVE);

    }

    private void showCard(Map<String ,String> info){
        String group_id = info.get("group_id");
        String user_id = info.get("user_id");
        this.updateConstant(group_id,user_id);
        Set<String> set_temp;
        String res = "";

        String redisKey = RedisKeyUtil.getKatchaUPSSRDetail(game,pool);
        if(redisTemplate.hasKey(redisKey)) {
            set_temp = redisTemplate.opsForSet().members(redisKey);
            res += "\n当前UP五星:\n";
            for(String s:set_temp) {
                res += " " + s;
            }
        } else {
            res += "\n未设置UP五星\n";
        }

        redisKey = RedisKeyUtil.getKatchaSSRDetail(game,pool);
        if(redisTemplate.hasKey(redisKey)) {
            set_temp = redisTemplate.opsForSet().members(redisKey);
            res += "\n当前常驻五星:\n";
            for(String s:set_temp) {
                res += " " + s;
            }
        } else {
            res += "\n未设置常驻五星\n";
        }

        redisKey = RedisKeyUtil.getKatchaSRDetail(game,pool);
        if(redisTemplate.hasKey(redisKey)) {
            set_temp = redisTemplate.opsForSet().members(redisKey);
            res += "\n当前四星:\n";
            for(String s:set_temp) {
                res += " " + s;
            }
        } else {
            res += "\n未设置四星\n";
        }

        redisKey = RedisKeyUtil.getKatchaRDetail(game,pool);
        if(redisTemplate.hasKey(redisKey)) {
            set_temp = redisTemplate.opsForSet().members(redisKey);
            res += "\n当前常驻三星:\n";
            for(String s:set_temp) {
                res += " " + s;
            }
        } else {
            res += "\n未设置常驻三星\n";
        }

        cqService.sendGroupMessage(info,res);
    }



    private void updateConstant(String group_id, String user_id) {
        game = (String) redisTemplate.opsForValue().get(RedisKeyUtil.getKatchaGame(group_id));
        pool = (String) redisTemplate.opsForValue().get(RedisKeyUtil.getKatchaPool(group_id));
        game = game == null ? DEFAULT_GAME : game;
        pool = pool == null ? DEFAULT_POOL : pool;
        this.historyKey = RedisKeyUtil.getKatchaHistory(game,pool,user_id);  // 内容存入String 具体内容
        this.historySSRKEY = RedisKeyUtil.getKatchaSSRHistory(game,pool,user_id); // 存入int,为在history中的位置
        this.historyUPSSRKEY = RedisKeyUtil.getKatchaUPSSRHistory(game,pool,user_id); // 存入int，为在histor中的位置
        this.historySRKEY = RedisKeyUtil.getKatchaSRHistory(game,pool,user_id); // 存入int，为在history中的位置
        this.pos = Math.toIntExact(redisTemplate.opsForList().size(historyKey));
        String userRedisKey = RedisKeyUtil.getKatchaUser(game,pool,user_id);
        BoundHashOperations operations = redisTemplate.boundHashOps(userRedisKey);
        Object temp;
        temp = operations.get("last_SSR");
        this.last_SSR = temp==null?0:(int)temp ;
        temp = operations.get("last_UP_SSR");
        this.last_UP_SSR = temp==null?0:(int)temp;
        temp = operations.get("last_SR");
        this.last_SR = temp==null?0:(int)temp ;
        temp = operations.get("pos");
        this.pos = temp==null?0:(int)temp ;
    }
    private int setNextIncr(String user_id) {
        String userRedisKey = RedisKeyUtil.getKatchaUser(game,pool,user_id);
        String redisKey = RedisKeyUtil.getKatcha(game,pool);
        int baodiTime = (int) redisTemplate.opsForHash().get(redisKey,"baodiTime");
        int incrTime = (int) redisTemplate.opsForHash().get(redisKey,"incrTime");
        int offset = baodiTime - incrTime;
        int next = new Random().nextInt(2*offset) - offset;
        next = Math.min(baodiTime,incrTime + next);
        redisTemplate.opsForHash().put(userRedisKey,"next_incr",next);
        return next;

    }

    private List<String > getKatcha(String user_id,int times) {
        List<String > res = new ArrayList<>();
        String redisKey = RedisKeyUtil.getKatcha(game,pool);
        String userRedisKey = RedisKeyUtil.getKatchaUser(game,pool,user_id);
        int baodiTime = (int) redisTemplate.opsForHash().get(redisKey,"baodiTime");
        Object temp = redisTemplate.opsForHash().get(userRedisKey,"next_incr");
        if(temp == null) {
            setNextIncr(user_id);
        }
        int incrTime = (int) redisTemplate.opsForHash().get(userRedisKey,"next_incr");
        double base_probability = (double) redisTemplate.opsForHash().get(redisKey,"base_probability");
        double SR_probability = (double) redisTemplate.opsForHash().get(redisKey,"SR_probability");

        for (int i = 0; i < times; ++i) {
            int get = getKatchaRare(baodiTime,incrTime,base_probability,SR_probability);
            String get_detail = "";
            String prefix = "";
            switch (get) {
                case GET_UP_SSR: redisTemplate.opsForList().rightPush(this.historyUPSSRKEY,this.pos);
                    redisTemplate.opsForList().rightPush(this.historySSRKEY,this.pos);
                    get_detail = getSSR(true);prefix = "☆☆☆";
                    incrTime = setNextIncr(user_id);break;
                case GET_SSR: redisTemplate.opsForList().rightPush(this.historySSRKEY,this.pos);
                    get_detail = getSSR(false);prefix = "☆☆☆";
                    incrTime = setNextIncr(user_id);break;
                case GET_SR: redisTemplate.opsForList().rightPush(this.historySRKEY,this.pos);
                    get_detail = getSR();prefix = "☆";break;
                default:get_detail = getR();break;
            }
            res.add(prefix + get_detail + prefix);
            redisTemplate.opsForList().rightPush(this.historyKey,get_detail);
        }


        redisTemplate.opsForHash().put(userRedisKey,"last_SSR",last_SSR);
        redisTemplate.opsForHash().put(userRedisKey,"last_UP_SSR",last_UP_SSR);
        redisTemplate.opsForHash().put(userRedisKey,"last_SR",last_SR);
        redisTemplate.opsForHash().put(userRedisKey,"pos",pos);

        return res;
    }

    private int getKatchaRare(int baodiTime, int incrTime,double base_probability,double SR_probability)  {
        double SSR_probability = base_probability;

        if(this.last_SSR >= incrTime) {
            SSR_probability += ((this.last_SSR - incrTime + 1))*((1-base_probability)/(double)(baodiTime-incrTime));
        }
        double sample = Math.random();
        int res = 0;
        if(sample<SSR_probability) {
            sample = Math.random();
            if(this.last_UP_SSR != this.last_SSR || sample<0.5) {
                res = GET_UP_SSR;
                this.last_UP_SSR = 0;
            } else {
                res = GET_SSR;
            }
            this.last_SSR = 0;
            ++this.last_SR;
        } else if (this.last_SR >= 9 || sample < SSR_probability + SR_probability) {
            res = GET_SR;
            this.last_SR = 0;
            ++ this.last_SSR;
            ++ this.last_UP_SSR;
        } else {
            ++ this.last_SSR;
            ++ this.last_UP_SSR;
            ++ this.last_SR;
            res = GET_R;
        }
        ++pos;
        return res;
    }

    private String getSSR(boolean isUP) {
        String redisKey = isUP?RedisKeyUtil.getKatchaUPSSRDetail(this.game,this.pool) : RedisKeyUtil.getKatchaSSRDetail(this.game,this.pool);
        Object temp = redisTemplate.opsForSet().randomMember(redisKey);
        if(temp == null) {
            return isUP?DEFAULT_UP_SSR:DEFAULT_SSR;
        }
        return (String) temp + String.format("(%d)",last_SSR);
    }

    private String getSR() {
        String redisKey = RedisKeyUtil.getKatchaSRDetail(this.game,this.pool);
        Object temp = redisTemplate.opsForSet().randomMember(redisKey);
        if(temp == null) {
            return DEFAULT_SR;
        }
        return (String) temp;
    }

    private String getR() {
        String redisKey = RedisKeyUtil.getKatchaRDetail(this.game,this.pool);
        Object temp = redisTemplate.opsForSet().randomMember(redisKey);
        if(temp == null) {
            return DEFAULT_R;
        }
        return (String) temp;
    }

    private String getCardDetail(String message) {
        message = message.replaceAll("#添加","").replaceAll("#删除","")
                .replaceAll("星","").trim();
        String  redisKey="";
        switch (message) {
            case "UP5":;
            case "UP五":redisKey = RedisKeyUtil.getKatchaUPSSRDetail(game,pool);break;
            case "5":;
            case "常驻5":;
            case "常驻五":;
            case "五":redisKey = RedisKeyUtil.getKatchaSSRDetail(game,pool);break;
            case "4":;
            case "四":redisKey = RedisKeyUtil.getKatchaSRDetail(game,pool);break;
            case "3":;
            case "三":redisKey = RedisKeyUtil.getKatchaRDetail(game,pool);break;
            default:break;
        }
        return redisKey;
    }

}
