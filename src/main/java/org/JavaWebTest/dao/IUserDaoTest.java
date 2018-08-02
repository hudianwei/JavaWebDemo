package org.JavaWebTest.dao;

import org.JavaWebTest.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
/*
* mybatis��spring��������Ҫдxml�ļ���ʹ��ע����ȥ������xml��д
*
* */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-MapperFactoryBean.xml"})
public class IUserDaoTest {
    @Autowired
    private IUserDao dao;

    @Test
    public void testSeletUser() throws Exception {
        System.out.println("�ϴ�");
        long id = 1;
        User user = dao.selectUser(id);
        System.out.print(user.getUsername());
    }

}
