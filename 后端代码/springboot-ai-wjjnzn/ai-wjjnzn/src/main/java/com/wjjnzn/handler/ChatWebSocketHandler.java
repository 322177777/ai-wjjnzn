package com.wjjnzn.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wjjnzn.entity.ChatUser;
import com.wjjnzn.entity.ReturnAI;
import com.wjjnzn.entity.Signature;
import com.wjjnzn.entity.SocketMessage;
import com.wjjnzn.util.AppConfig;
import com.wjjnzn.util.LoginUtil;
import com.wjjnzn.util.domain.ReturnObject;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.*;

import java.io.IOException;

@Component
public class ChatWebSocketHandler implements WebSocketHandler {
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // 在客户端连接时调用，可以进行一些初始化操作

    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        // 当接收到客户端消息时调用，对消息进行处理
        ObjectMapper objectMapper = new ObjectMapper();
        String receivedMessage = message.getPayload().toString();
        SocketMessage messageData = null;
        try {
            messageData = objectMapper.readValue(receivedMessage, SocketMessage.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //获取用户ID和输入信息
        String userId = messageData.getUserId();
        String content = messageData.getContent();
        System.out.println(userId + content);
        //初始化
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate2 = new RestTemplate();
        //获取signature
        String getSignatureUrl = LoginUtil.GET_SIGNATURE + LoginUtil.AI_TOKEN;
        ChatUser chatUser = new ChatUser();
        chatUser.setUserid(userId);
        HttpEntity<ChatUser> requestEntity = new HttpEntity<>(chatUser, headers);
        ResponseEntity<String> response = restTemplate2.postForEntity(getSignatureUrl, requestEntity, String.class);
        System.out.println(response);
        String responseBody = response.getBody();
        // 解析 JSON 字符串，获取指定字段的值
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = mapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String signature = jsonNode.get("signature").asText(); // 获取 signature 属性值

        //调用智能对话接口
        String AIUrl = LoginUtil.GET_AI + LoginUtil.AI_TOKEN;
        ChatUser chatUser2 = new ChatUser();
        chatUser2.setQuery(content);
        chatUser2.setSignature(signature);

        HttpEntity<ChatUser> requestEntity2 = new HttpEntity<>(chatUser2, headers);
        ResponseEntity<String> response2 = restTemplate2.postForEntity(AIUrl, requestEntity2, String.class);
        String responseBody2 = response2.getBody();
        JsonNode jsonNode2 = null;
        try {
            jsonNode2 = mapper.readTree(responseBody2);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //回答
        String answer = jsonNode2.get("answer").asText();
        System.out.println("回答："+answer);
        //调用service层方法，实现持久化存储
        //...
        //返回结果
        // 将响应数据发送给客户端
        TextMessage textMessage = new TextMessage(answer);
        try {
            session.sendMessage(textMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        // 处理 WebSocket 传输错误，如网络断开等

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        // 在客户端关闭连接时调用，进行一些清理工作

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}

