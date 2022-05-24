package com.QQRobot.demo.Utils;

public interface ConstantUtil {
    /*
     * 自动回复功能说明
     * */
    String AUTO_REPEAT_INFO = "自动回复功能说明：\n" +
            "#学习/#严谨/#魔怔[空格]关键词[空格]自动回复内容 \n " +
            "#学习:对话中存在关键词，则起反应\n" +
            "#严谨:对话中只有关键词，才起反应\n" +
            "#魔怔:对话中包含关键词所有单字且字先后顺序一致就会起反应\n" +
            "复读:连续两句话相同，则跟进+1";
    String FAST_LEARNING_INFO = "快速学习功能说明：\n" +
            "#快速学习/#快速严谨/#快速魔怔[空格]关键词\n " +
            "将提取你发送的上一条信息作为自动回复内容";
    String REMEMBER_REPEAT_INFO = "备忘录功能说明：#备忘录 关键词 内容（可选，无内容则将你的上一句话当做内容）\n" +
            "同一用户的同一关键词备忘录惟一，重复添加会覆盖，且优先级在自动回复前\n";
    String HELP_INFO = "功能介绍：\n" +
            "#学习 \n" +
            "#快速学习\n" +
            "#添加瑟(涩)图" +
            "#瑟(涩)图or#来张瑟(涩)图[空格]所需数量[可选,默认为1]\n" +
            "#瑟(涩)图鸿儒\n" +
            "#瑟图总数";
    String EASY_LEARNING_FAIL = "你不输点东西进来，叔叔我很为难啊";
    String HSCENCE_ADD_INFO ="#添加瑟(涩)图[空格]你的图片\n" +
            "可一次性添加多张";
    String HSCENCE_SUPER_INFO = "您将进入瑟图鸿儒模式，请注意此后您发的每一张图都会存入瑟图库，如果不想污染瑟图库，请三思而后行\n" +
            "输入 \"#贤者模式\"以退出鸿儒模式";
    String HSCENCE_SUPER_ERROR_INFO = "您正在鸿儒模式，请勿尝试其他带#的操作，认真点发瑟图\n" +
            "输入 \"#贤者模式\"以退出鸿儒模式";
    String HSCENCE_SUPER_OUT = "\"呼~~~~(意味深)\"机器人酱如此说道，标志着您的鸿儒已经结束";
    String HSCENCE_NO_FOUND = "瑟图模块正在维护，请稍后";
    int HSCENCE_INSERT = 10;
}
