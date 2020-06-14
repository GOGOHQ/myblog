package com.hq.myblog.Service;

import com.hq.myblog.Po.User;
import org.springframework.stereotype.Service;


public interface UserService {
    User checkUser(String name, String passWord);
}
