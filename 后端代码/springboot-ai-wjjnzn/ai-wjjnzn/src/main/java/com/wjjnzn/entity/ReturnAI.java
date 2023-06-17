package com.wjjnzn.entity;

import lombok.Data;

@Data
public class ReturnAI {
    private int ans_node_id;       //技能id
    private String ans_node_name;  //分类/技能名称
    private String answer;          //命中的回答
    private String from_user_name;  //发起query的用户,对应签名接口的userid
    private String title;           //标准问题/意图名称
    private String query;           //用户发送的消息
    private String to_user_name;    //接受query的机器人
}
