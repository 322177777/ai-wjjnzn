package com.wjjnzn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;         // 非业务ID
    private String token;       // token
    private String openId;      // 用户openId
    private String sessionKey;  // 用户sessionKey
    private String unionId;     // 小程序unionId
    private String nickname;    // 用户昵称
    private String avatarUrl;   // 用户头像url
}
