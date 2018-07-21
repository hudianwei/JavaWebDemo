package com.JavaWebTest.dao;

import com.JavaWebTest.model.User;

public interface IUserDao {
    User selectUser(long userid);
}
