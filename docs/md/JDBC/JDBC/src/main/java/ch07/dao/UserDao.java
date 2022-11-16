package ch07.dao;

import ch07.entity.User;

import java.sql.Connection;

/**
 * @author DeYou
 * @date 2022/8/29
 */
public interface UserDao {

    /**
     * 根据User对象中的用户名和密码从数据库中获取一条记录
     *
     * @param user
     * @return User 数据库中有记录 null 数据库中无此记录
     */
    User getUser(Connection conn,User user);

    /**
     * 根据User对象中的用户名从数据库中获取一条记录
     *
     * @param user
     * @return true 数据库中有记录 false 数据库中无此记录
     */
    boolean checkUsername(Connection conn,User user);

    /**
     * 向数据库中插入User对象
     *
     * @param user
     */
    void saveUser(Connection conn, User user);
}