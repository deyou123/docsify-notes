package ch05;

import org.junit.Test;
import utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * @author DeYou
 * @date 2022/8/29
 */
public class BatchTest {
    @Test
    public void testBatch1() throws Exception {
        Instant bengin = Instant.now();
        Connection conn = JDBCUtils.getConnection();
        Statement st = conn.createStatement();

        for(int i = 1;i <= 20000;i++){
            String sql = "insert into goods(name) values('name_"+i+"')";

            st.executeUpdate(sql);
        }
        Instant end = Instant.now();
        long time = ChronoUnit.MILLIS.between( bengin, end );
        System.out.println("time: " + time+"millis");//1000  time 7617    20000 time: 129738millis

        JDBCUtils.closeResource(conn, st);
    }
    @Test
   public void test2() throws Exception {
        Instant bengin = Instant.now();
        Connection conn = JDBCUtils.getConnection();

        String sql = "insert into goods(name)values(?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        for(int i = 1;i <= 20000;i++){
            ps.setString(1, "name_" + i);
            ps.executeUpdate();
        }

        Instant end = Instant.now();
        long time = ChronoUnit.MILLIS.between( bengin, end );
        System.out.println("time: " + time+"millis"); //7574
        JDBCUtils.closeResource(conn, ps);
   }

    @Test
    public void test3() throws Exception {
        Instant bengin = Instant.now();
        Connection conn = JDBCUtils.getConnection();

        String sql = "insert into goods(name)values(?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        for(int i = 1;i <= 20000;i++){
            ps.setString(1, "name_" + i);

            //1.“攒”sql
            ps.addBatch();
            if(i % 500 == 0){
                //2.执行
                ps.executeBatch();
                //3.清空
                ps.clearBatch();
            }
        }

        Instant end = Instant.now();
        long time = ChronoUnit.MILLIS.between( bengin, end );
        System.out.println("time: " + time+"millis"); //7700
        JDBCUtils.closeResource(conn, ps);
    }
    @Test
    public void test4() throws Exception {
        Instant bengin = Instant.now();
        Connection conn = JDBCUtils.getConnection();

        //1.设置为不自动提交数据
        conn.setAutoCommit(false);

        String sql = "insert into goods(name)values(?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        for(int i = 1;i <= 20000;i++){
            ps.setString(1, "name_" + i);

            //1.“攒”sql
            ps.addBatch();

            if(i % 500 == 0){
                //2.执行
                ps.executeBatch();
                //3.清空
                ps.clearBatch();
            }
        }

        //2.提交数据
        conn.commit();
        Instant end = Instant.now();
        long time = ChronoUnit.MILLIS.between( bengin, end );
        System.out.println("time: " + time+"millis"); //1534
        JDBCUtils.closeResource(conn, ps);
    }
}