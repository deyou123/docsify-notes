package ch09;

import ch03.entity.Customer;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;
import utils.JDBCUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;

/**
 * @author DeYou
 * @date 2022/8/29
 */
public class DBUtilsTest {

    @Test
    public void testInsert() throws Exception {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection();
        String sql = "insert into customers(name,email,birth)values(?,?,?)";
        int count = runner.update(conn, sql, "何成飞", "he@qq.com", "1992-09-08");

        System.out.println("添加了" + count + "条记录");

        JDBCUtils.closeResource(conn, null);

    }

    // 测试删除
    @Test
    public void testDelete() throws Exception {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection();
        String sql = "delete from customers where id < ?";
        int count = runner.update(conn, sql,3);

        System.out.println("删除了" + count + "条记录");

        JDBCUtils.closeResource(conn, null);

    }

    /*
     * 测试查询:查询一条记录
     *
     * 使用ResultSetHandler的实现类：BeanHandler
     */
    @Test
    public void testQueryInstance() throws Exception{
        QueryRunner runner = new QueryRunner();

        Connection conn = JDBCUtils.getConnection();

        String sql = "select id,name,email,birth from customers where id = ?";

        //
        BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
        Customer customer = runner.query(conn, sql, handler, 25);
        System.out.println(customer);
        JDBCUtils.closeResource(conn, null);
    }

    /*
     * 测试查询:查询多条记录构成的集合
     *
     * 使用ResultSetHandler的实现类：BeanListHandler
     */
    @Test
    public void testQueryList() throws Exception{
        QueryRunner runner = new QueryRunner();

        Connection conn = JDBCUtils.getConnection();

        String sql = "select id,name,email,birth from customers where id < ?";

        //
        BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);
        List<Customer> list = runner.query(conn, sql, handler, 23);
        list.forEach(System.out::println);

        JDBCUtils.closeResource(conn, null);
    }

    /*
     * 自定义ResultSetHandler的实现类
     */
    @Test
    public void testQueryInstance1() throws Exception{
        QueryRunner runner = new QueryRunner();

        Connection conn = JDBCUtils.getConnection();

        String sql = "select id,name,email,birth from customers where id = ?";

        ResultSetHandler<Customer> handler = new ResultSetHandler<Customer>() {

            @Override
            public Customer handle(ResultSet rs) throws SQLException {
                System.out.println("handle");
//			return new Customer(1,"Tom","tom@126.com",new Date(123323432L));

                if(rs.next()){
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    Date birth = rs.getDate("birth");

                    return new Customer(id, name, email, birth);
                }
                return null;

            }
        };

        Customer customer = runner.query(conn, sql, handler, 29);

        System.out.println(customer);

        JDBCUtils.closeResource(conn, null);
    }

    /*
     * 如何查询类似于最大的，最小的，平均的，总和，个数相关的数据，
     * 使用ScalarHandler
     *
     */
    @Test
    public void testQueryValue() throws Exception{
        QueryRunner runner = new QueryRunner();

        Connection conn = JDBCUtils.getConnection();

        //测试一：
//	String sql = "select count(*) from customers where id < ?";
//	ScalarHandler handler = new ScalarHandler();
//	long count = (long) runner.query(conn, sql, handler, 20);
//	System.out.println(count);

        //测试二：
        String sql = "select max(birth) from customers";
        ScalarHandler handler = new ScalarHandler();
        Date birth = (Date) runner.query(conn, sql, handler);
        System.out.println(birth);

        JDBCUtils.closeResource(conn, null);
    }
}