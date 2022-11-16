package ch08;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @author DeYou
 * @date 2022/8/29
 */
public class Cp30Test2 {

    private static DataSource cpds = new ComboPooledDataSource("helloc3p0");
    public static Connection getConnection2() throws SQLException {
        Connection conn = cpds.getConnection();
        return conn;
    }


    @Test
    public void test()  throws Exception {
        Connection connection = Cp30Test2.getConnection2();
        System.out.println(!Objects.isNull( connection ) );
    }
}