package com.JavaWebTest.service.Impl;

import com.JavaWebTest.dao.IUserDao;
import com.JavaWebTest.model.User;
import com.JavaWebTest.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("userService")
public class UserServiceimpl implements IUserService {
    @Resource
    private IUserDao userDao;
    public User selectUser(long userid) {
        return this.userDao.selectUser(userid);
    }
}
