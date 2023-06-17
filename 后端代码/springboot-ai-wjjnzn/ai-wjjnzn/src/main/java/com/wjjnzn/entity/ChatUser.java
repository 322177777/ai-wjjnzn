package com.wjjnzn.entity;

import lombok.Data;

@Data
public class ChatUser {
    private int id;
    private String userid;
    private String signature;
    private String query;
}
