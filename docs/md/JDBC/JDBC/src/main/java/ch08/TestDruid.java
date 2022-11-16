package ch08;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author DeYou
 * @date 2022/8/29
 */
public class TestDruid {

    public static Connection getConnection5() throws Exception {
        Properties pro = new Properties();
        pro.load(TestDruid.class.getClassLoader().getResourceAsStream("druid.properties"));
        DataSource ds = DruidDataSourceFactory.createDataSource(pro);
        return  ds.getConnection();
    }
}