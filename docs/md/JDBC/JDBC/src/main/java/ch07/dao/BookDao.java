package ch07.dao;

import ch07.entity.Book;

import java.sql.Connection;
import java.util.List;

/**
 * @author DeYou
 * @date 2022/8/29
 */
public interface BookDao {

    /**
     * 从数据库中查询出所有的记录
     *
     * @return
     */
    List<Book> getBooks(Connection conn);

    /**
     * 向数据库中插入一条记录
     *
     * @param book
     */
    void saveBook(Connection conn,Book book);

    /**
     * 从数据库中根据图书的id删除一条记录
     *
     * @param bookId
     */
    void deleteBookById(Connection conn,String bookId);

    /**
     * 根据图书的id从数据库中查询出一条记录
     *
     * @param bookId
     * @return
     */
    Book getBookById(Connection conn,String bookId);

    /**
     * 根据图书的id从数据库中更新一条记录
     *
     * @param book
     */
    void updateBook(Connection conn,Book book);

    /**
     * 获取带分页的图书信息
     *
     * @param page：是只包含了用户输入的pageNo属性的page对象
     * @return 返回的Page对象是包含了所有属性的Page对象
     */
    Page<Book> getPageBooks(Connection conn,Page<Book> page);

    /**
     * 获取带分页和价格范围的图书信息
     *
     * @param page：是只包含了用户输入的pageNo属性的page对象
     * @return 返回的Page对象是包含了所有属性的Page对象
     */
    Page<Book> getPageBooksByPrice(Connection conn, Page<Book> page, double minPrice, double maxPrice);

}