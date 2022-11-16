package ch08;

import org.apache.commons.dbcp2.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author DeYou
 * @date 2022/8/29
 */
public class DBCPTest2 {
    private static DataSource source = null;
    static{
        try {
            Properties pros = new Properties();

            InputStream is = DBCPTest1.class.getClassLoader().getResourceAsStream("dbcp.properties");

            pros.load(is);
            //根据提供的BasicDataSourceFactory创建对应的DataSource对象
            source = BasicDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static Connection getConnection4() throws Exception {

        Connection conn = source.getConnection();

        return conn;
    }
}