package org.JavaWebTest.dao;

import org.JavaWebTest.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
/*
* mybatis和spring整合是需要写xml文件或使用注解免去大量的xml编写
*
* */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-MapperFactoryBean.xml"})
public class IUserDaoTest {
    @Autowired
    private IUserDao dao;

    @Test
    public void testSeletUser() throws Exception {
        System.out.println("上传");
        long id = 1;
        User user = dao.selectUser(id);
        System.out.print(user.getUsername());
    }

}
