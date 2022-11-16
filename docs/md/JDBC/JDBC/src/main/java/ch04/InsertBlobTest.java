package ch04;

import ch03.entity.Customer;
import org.junit.Test;
import utils.JDBCUtils;

import java.io.*;
import java.sql.*;

import static utils.JDBCUtils.getConnection;

/**
 * @author DeYou
 * @date 2022/8/29
 */
public class InsertBlobTest {
    @Test
    public void insertBlob() throws Exception {
        //获取连接
        Connection conn = getConnection();

        String sql = "insert into customers(name,email,birth,photo)values(?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);

// 填充占位符
        ps.setString(1, "徐海强");
        ps.setString(2, "xhq@126.com");
        ps.setDate(3, new Date(new java.util.Date().getTime()));
// 操作Blob类型的变量
        InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream( "photo/girl.jpg" );

        ps.setBlob(4, inputStream);
//执行
        ps.execute();

        inputStream.close();
        JDBCUtils.closeResource(conn, ps);
    }
    @Test
    public void testUpdate() throws Exception {
        Connection conn = getConnection();
        String sql = "update customers set photo = ? where id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);

// 填充占位符
// 操作Blob类型的变量
        InputStream ins = ClassLoader.getSystemClassLoader().getResourceAsStream( "photo/playgirl.jpg" );
        ps.setBlob(1, ins);
        ps.setInt(2, 20);

        ps.execute();

        ins.close();
        JDBCUtils.closeResource(conn, ps);
    }
    @Test
    public void testReadBlob()throws Exception {
        String sql = "SELECT id, name, email, birth, photo FROM customers WHERE id = ?";
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, 25);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            Integer id = rs.getInt(1);
            String name = rs.getString(2);
            String email = rs.getString(3);
            Date birth = rs.getDate(4);
            Customer cust = new Customer(id, name, email, birth);
            System.out.println(cust);
            //读取Blob类型的字段
            Blob photo = rs.getBlob( 5 );
            InputStream is = photo.getBinaryStream();
            OutputStream os = new FileOutputStream("src/main/resources/c.jpg");
            byte [] buffer = new byte[1024];
            int len = 0;
            while((len = is.read(buffer)) != -1){
                os.write(buffer, 0, len);
            }
            JDBCUtils.closeResource(conn, ps, rs);

            if(is != null){
                is.close();
            }

            if(os !=  null){
                os.close();
            }

        }

    }
}