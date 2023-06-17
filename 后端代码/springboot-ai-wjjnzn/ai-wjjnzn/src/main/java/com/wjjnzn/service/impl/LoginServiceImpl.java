package com.wjjnzn.service.impl;

import com.wjjnzn.entity.User;
import com.wjjnzn.mapper.LoginMapper;
import com.wjjnzn.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginMapper loginMapper;

    @Override
    public int addLoginUser(User user) {
        return loginMapper.addUser(user);
    }
}
