package ch09;

import ch08.TestDruid;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author DeYou
 * @date 2022/8/29
 */
public class TestUpdateWithTx {
    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn =TestDruid.getConnection5();
            conn.setAutoCommit( false );
            String sql = "update users set username = ? where id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString( 1,"红孩儿111" );
            ps.setInt( 2, 1 );
            ps.execute();
        } catch (Exception e) {
            throw new RuntimeException( e );
        }finally {
            try {
                conn.commit();
            } catch (SQLException e) {
                throw new RuntimeException( e );
            }
        }
    }
}