package com.hq.myblog.Service;

import com.hq.myblog.Dao.UserRepository;
import com.hq.myblog.Po.User;
import com.hq.myblog.Utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Override
    public User checkUser(String userName, String passWord) {
        User user = userRepository.findByUserNameAndPassWord(userName, MD5Utils.code(passWord));
        return user;
    }
}
