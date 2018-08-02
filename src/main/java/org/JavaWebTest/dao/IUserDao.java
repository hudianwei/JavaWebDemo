package org.JavaWebTest.dao;

import org.JavaWebTest.model.User;
import org.apache.ibatis.annotations.Select;

public interface IUserDao {
    @Select("select * from user where id=#{id}")
    User selectUser(long userid);
}
