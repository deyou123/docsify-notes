package ch08;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Test;

import java.sql.Connection;
import java.util.Objects;

/**
 * @author DeYou
 * @date 2022/8/29
 */
public class Cp30Test1 {

    //使用C3P0数据库连接池的方式，获取数据库的连接：不推荐
    public static Connection getConnection1() throws Exception{
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass("com.mysql.jdbc.Driver");
        cpds.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        cpds.setUser("root");
        cpds.setPassword("666666");

//	cpds.setMaxPoolSize(100);

        Connection conn = cpds.getConnection();
        return conn;
    }


    @Test
    public void test()  throws Exception {
        Connection connection1 = Cp30Test1.getConnection1();
        System.out.println(!Objects.isNull( connection1 ) );
    }
}