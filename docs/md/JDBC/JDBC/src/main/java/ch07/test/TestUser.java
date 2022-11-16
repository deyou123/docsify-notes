package ch07.test;

import ch07.dao.UserDao;
import ch07.dao.UserDaoImpl;
import ch07.entity.User;
import ch08.*;
import org.junit.Test;
import utils.JDBCUtils;

import java.sql.Connection;

/**
 * @author DeYou
 * @date 2022/8/29
 */
public class TestUser {
    @Test
    public void testUser() throws Exception {
        Connection connection = JDBCUtils.getConnection();
         connection = Cp30Test1.getConnection1();
         connection = Cp30Test2.getConnection2();
         connection = DBCPTest1.getConnection3();
         connection = DBCPTest2.getConnection4();
         connection = DBCPTest2.getConnection4();
         connection = TestDruid.getConnection5();

        User user = new User();

        user.setUsername("zhangsan");
        user.setPassword("123456");
        user.setEmail("zhangsan@qq.com");

        UserDao userDao = new UserDaoImpl();
        //userDao.saveUser( connection, user );
        User user1 = userDao.getUser( connection, user );
        System.out.println(user1);
        connection.close();
    }
}