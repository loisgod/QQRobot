package com.QQRobot.demo.Utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

// katcha:{游戏}:{池子}
public class KatchaSystem {
    List<String> history = new ArrayList<>();
    List<Integer> SSR_history = new ArrayList<>();
    List<Integer> UP_SSR_history = new ArrayList<>();
    List<Integer> SR_history = new ArrayList<>();
    private final String DEFAULT_SSR = "☆☆☆五星歪啦☆☆☆";
    private final String DEFAULT_UP_SSR = "☆☆☆UP五星☆☆☆";
    private final String DEFAULT_SR = "☆四星☆";
    private final String DEFAULT_R = "三星";
    private int current=0;

    int last_SSR = 0;
    int last_up_SSR = 0;
    int last_SR = 0;


    public String getKatcha(int baodiTime, int incrTime,double base_probability,double SR_probability) {
        double SSR_probability = base_probability;
        if(last_SSR >= incrTime) {
            SSR_probability += ((last_SSR - incrTime + 1))*((1-base_probability)/(double)(baodiTime-incrTime));
        }
        double sample = Math.random();
        String res = "";
        if(sample<SSR_probability) {
            sample = Math.random();
            if(last_up_SSR != last_SSR || sample<0.5) {
                res = DEFAULT_UP_SSR;
                UP_SSR_history.add(current+1);
                last_up_SSR = 0;
            } else {
                res = DEFAULT_SSR;
            }
            SSR_history.add(current+1);
            last_SSR = 0;
            ++last_SR;
        } else if (last_SR >= 9 || sample < SSR_probability + SR_probability) {
            res =DEFAULT_SR;
            SR_history.add(current+1);
            last_SR = 0;
            ++ last_SSR;
            ++ last_up_SSR;
        } else {
            ++ last_SSR;
            ++ last_up_SSR;
            ++ last_SR;
            res = DEFAULT_R;
        }
        history.add(res);
        ++current;
        return res;
    }

    public List<String> getMulKatcha(int baodiTime, int incrTime,double base_probability,double SR_probability,int times,boolean simple) {
        int last = this.SSR_history.size()==0?this.history.size():this.history.size()-this.SSR_history.get(this.SSR_history.size()-1);
        List<String> history = new ArrayList<>();
        List<Integer> SSR_history = new ArrayList<>();
        List<Integer> UP_SSR_history = new ArrayList<>();
        List<Integer> SR_history = new ArrayList<>();
        List<String > res = new ArrayList<>();
        String r;
        for(int i=0;i<times;++i) {
            r = getKatcha(baodiTime,incrTime,base_probability,SR_probability);
            switch (r) {
                case DEFAULT_UP_SSR:UP_SSR_history.add(last + i);
                case DEFAULT_SSR:SSR_history.add(last + i);break;
                case DEFAULT_SR:SR_history.add(i);break;
                default:break;
            }
            history.add(r);
            res.add(r);
        }
        last = this.SSR_history.size()==0?this.history.size():this.history.size()-this.SSR_history.get(this.SSR_history.size()-1);
        res.add("距离上次五星 " + last);
        if (simple) {
            return new ArrayList<String>(){{add(getInfo(SSR_history,UP_SSR_history,SR_history,history));}};
        } else {
            return res;
        }
    }

    public String getInfo() {
        return this.getInfo(this.SSR_history,this.UP_SSR_history,this.SR_history,this.history);
    }

    public String getInfo(List<Integer> SSR_history, List<Integer> UP_SSR_history,List<Integer > SR_history,List<String> history) {
        String global_info = String.format("共抽取%d抽\n共获得%d个五星\n其中%d个UP五星\n共获得%d个四星\n五星出货率%f%%\n五星歪率%f%%\n",history.size(),
                SSR_history.size(),UP_SSR_history.size(),SR_history.size(),(float)SSR_history.size()*100/(float)history.size(),
                (float)(SSR_history.size()-UP_SSR_history.size())*100/(float)SSR_history.size());
        String detail = "出货详情(第几发出货)：";
        int j=0;
        if(!SSR_history.isEmpty()) {
            detail = detail + SSR_history.get(0);
            if (!UP_SSR_history.isEmpty() && UP_SSR_history.get(0).equals(SSR_history.get(0))) {
                ++j;
            } else {
                detail = detail + "(歪)";
            }
        }
        for(int i=1;i<SSR_history.size();++i) {
            detail =detail + " " +(SSR_history.get(i) - SSR_history.get(i-1));
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




}
