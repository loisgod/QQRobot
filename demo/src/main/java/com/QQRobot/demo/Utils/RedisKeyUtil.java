package com.QQRobot.demo.Utils;

public class RedisKeyUtil {

    public static final String SPLIT=":";
    public static final String PREFIX_KATCHA = "katcha";
    public static final String PREFIX_GAME = "game";
    public static final String PREFIX_POOL = "pool";
    public static final String HISTORY = "history";
    public static final String SSR_HISTORY = "SSR_history";
    public static final String UP_SSR_HISTORY = "UP_SSR_history";
    public static final String SR_HISTORY = "SR_history";
    public static final String SSR_DETAIL = "SSR_detail";
    public static final String UP_SSR_DETAIL = "UP_SSR_DETAIL";
    public static final String SR_DETAIL = "SR_detail";
    public static final String R_DETAIL = "R_detail";
    public static final String PREFIX_LAST_INFO = "last_info";

    public static String getKatchaGame(String group_id) {
        return PREFIX_GAME + SPLIT + group_id;
    }

    public static String getKatchaPool(String group_id) {
        return PREFIX_POOL + SPLIT + group_id;
    }

    public static String getKatcha(String game,String pool) {
        return PREFIX_KATCHA + SPLIT + game + SPLIT + pool;
    }

    public static String getKatchaUser(String game,String pool,String userId) {
        return PREFIX_KATCHA + SPLIT + game + SPLIT + pool + SPLIT + userId;
    }

    public static String getKatchaTarget(String game,String pool,String data) {
        return PREFIX_KATCHA + SPLIT + game + SPLIT + pool + (data==null?"":SPLIT + data);
    }

    public static String getKatchaHistory(String game,String pool,String userId) {
        return PREFIX_KATCHA + SPLIT + game + SPLIT + pool + SPLIT + HISTORY + SPLIT + userId;
    }

    public static String getKatchaSSRHistory(String game,String pool,String userId) {
        return PREFIX_KATCHA + SPLIT + game + SPLIT + pool + SPLIT + SSR_HISTORY + SPLIT + userId;
    }

    public static String getKatchaUPSSRHistory(String game,String pool,String userId) {
        return PREFIX_KATCHA + SPLIT + game + SPLIT + pool + SPLIT + UP_SSR_HISTORY + SPLIT + userId;
    }

    public static String getKatchaSRHistory(String game,String pool,String userId) {
        return PREFIX_KATCHA + SPLIT + game + SPLIT + pool + SPLIT + SR_HISTORY + SPLIT + userId;
    }

    public static String getKatchaSSRDetail(String game,String pool) {
        return PREFIX_KATCHA + SPLIT + game + SPLIT + pool + SPLIT + SSR_DETAIL  ;
    }

    public static String getKatchaUPSSRDetail(String game,String pool) {
        return PREFIX_KATCHA + SPLIT + game + SPLIT + pool + SPLIT + UP_SSR_DETAIL  ;
    }

    public static String getKatchaSRDetail(String game,String pool) {
        return PREFIX_KATCHA + SPLIT + game + SPLIT + pool + SPLIT + SR_DETAIL  ;
    }

    public static String getKatchaRDetail(String game,String pool) {
        return PREFIX_KATCHA + SPLIT + game + SPLIT + pool + SPLIT + R_DETAIL  ;
    }
    public static String getLastInfo(String group_id,String user_id) {
        return PREFIX_LAST_INFO + SPLIT + group_id + SPLIT + user_id;
    }





    // katcha:{游戏}:{池子} -> KatchaUtil


}
