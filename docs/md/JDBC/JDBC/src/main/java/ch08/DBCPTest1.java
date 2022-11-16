package ch08;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;

/**
 * @author DeYou
 * @date 2022/8/29
 */
public class DBCPTest1 {
    public static Connection getConnection3() throws Exception {
        BasicDataSource source = new BasicDataSource();

        source.setDriverClassName("com.mysql.jdbc.Driver");
        source.setUrl("jdbc:mysql:///test");
        source.setUsername("root");
        source.setPassword("666666");

        //
        source.setInitialSize(10);

        Connection conn = source.getConnection();
        return conn;
    }
}