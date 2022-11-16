package utils;

import org.junit.Test;

import java.sql.Connection;
import java.util.Objects;

/**
 * @author DeYou
 * @date 2022/8/29
 */
public class ConnectTest {
    @Test
    public void connect() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        System.out.println( !Objects.isNull( connection ) );
    }
}