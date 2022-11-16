package ch07.dao;

import ch07.entity.User;

import java.sql.Connection;

/**
 * @author DeYou
 * @date 2022/8/29
 */
public class UserDaoImpl extends BaseDao<User> implements UserDao{
    @Override
    public User getUser(Connection conn, User user) {

        // 调用BaseDao中获取一个对象的方法
        User bean = null;
        // 写sql语句
        String sql = "select id,username,password,email from users where username = ? and password = ?";
        bean = getBean(conn,sql, user.getUsername(), user.getPassword());
        return bean;
    }

    @Override
    public boolean checkUsername(Connection conn, User user) {
        // 调用BaseDao中获取一个对象的方法
        User bean = null;
        // 写sql语句
        String sql = "select id,username,password,email from users where username = ?";
        bean = getBean(conn,sql, user.getUsername());
        return bean != null;
    }

    @Override
    public void saveUser(Connection conn, User user) {
        //写sql语句
        String sql = "insert into users(username,password,email) values(?,?,?)";
        //调用BaseDao中通用的增删改的方法
        update(conn,sql, user.getUsername(),user.getPassword(),user.getEmail());
    }

}