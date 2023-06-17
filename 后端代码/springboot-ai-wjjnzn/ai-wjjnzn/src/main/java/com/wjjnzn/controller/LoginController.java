package com.wjjnzn.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wjjnzn.entity.User;
import com.wjjnzn.service.LoginService;
import com.wjjnzn.util.AppConfig;
import com.wjjnzn.util.LoginUtil;
import com.wjjnzn.util.domain.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@RestController
public class LoginController {


    @Autowired
    private LoginService loginService;

    @GetMapping("/login")
    public ReturnObject toLogin(@RequestParam("code") String code, @RequestParam("nickname") String nickname, @RequestParam("avatarUrl") String avatarUrl) throws IOException {
        System.out.println("名字："+nickname);
        ReturnObject returnObject = new ReturnObject();
        //发送HTTP请求
        RestTemplate restTemplate = new AppConfig().restTemplate();
        // 发起 GET 请求，并返回 JSON 数据
        // 调用微信API，获取session_key/openid/unionid
        String url = LoginUtil.REQUEST_URL + "?appid=" + LoginUtil.APP_ID + "&secret=" + LoginUtil.APP_SECRET + "&js_code=" + code + "&grant_type=" + LoginUtil.GRANT_TYPE;
        String response = restTemplate.getForObject(url, String.class);
        Map<String, Object> result = new ObjectMapper().readValue(response, new TypeReference<Map<String, Object>>() {
        });
        String openId = (String) result.get("openid");
        String sessionKey = (String) result.get("session_key");
        String unionId = (String) result.get("unionid");

        //调用API获取小程序token
        RestTemplate restTemplate2 = new AppConfig().restTemplate2();
        String tokenUrl = LoginUtil.TOKEN_URL + "?appid=" + LoginUtil.APP_ID + "&secret=" + LoginUtil.APP_SECRET + "&grant_type=" + LoginUtil.GRANT_TYPE2;
        Map<String, Object> tokenMap = restTemplate2.getForObject(tokenUrl, Map.class);
        String accessToken = (String) tokenMap.get("access_token");

        if (nickname == null) {
            nickname = "-";
        }
        if (avatarUrl == null) {
            avatarUrl = "-";
        }
        // 封装参数
        User user = new User();
        user.setToken(accessToken);
        user.setOpenId(openId);
        user.setSessionKey(sessionKey);
        user.setUnionId(unionId);
        user.setNickname(nickname);
        user.setAvatarUrl(avatarUrl);
        System.out.println(user.toString());

        //调用service层方法,返回结果
        int ret = loginService.addLoginUser(user);
        if (ret > 0) {
            returnObject.setCode(LoginUtil.SUCCESS);
            returnObject.setMessage("登录成功");
            returnObject.setRetData(user);
        } else {
            returnObject.setCode(LoginUtil.FAIL);
            returnObject.setMessage("网络错误,请稍后重试...");
        }
        return returnObject;
    }
}
