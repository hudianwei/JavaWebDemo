package org.JavaWebTest.service.Impl;

import org.JavaWebTest.dao.IUserDao;
import org.JavaWebTest.model.User;
import org.JavaWebTest.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceimpl implements IUserService {
    @Resource
    private IUserDao userDao;
    public User selectUser(long userid) {
        return this.userDao.selectUser(userid);
    }
}
