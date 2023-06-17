package com.wjjnzn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjjnzn.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface LoginMapper extends BaseMapper<User> {
    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into user(token,open_id,session_key,union_id,nickname,avatar_url) " +
            "values(#{token},#{openId},#{sessionKey},#{unionId},#{nickname},#{avatarUrl})")
    int addUser(User user);

}
