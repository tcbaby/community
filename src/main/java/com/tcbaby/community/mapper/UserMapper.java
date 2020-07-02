package com.tcbaby.community.mapper;

import com.tcbaby.community.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author tcbaby
 * @date 20/05/04 15:37
 */
@Repository
public interface UserMapper {
    User selectById(int id);

    User selectByUsername(String username);

    User selectByEmail(String email);

    int save(User user);

    int updateStatusById(int id, int status);

    int updateHeaderUrlById(int id, String headerUrl);

    int updatePasswordById(int id, String password);
}