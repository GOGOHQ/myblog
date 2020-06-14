package com.hq.myblog.Dao;

import com.hq.myblog.Po.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUserNameAndPassWord(String name, String passWord);
}
