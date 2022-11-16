package ch07.dao;

import ch07.entity.Book;

import java.sql.Connection;
import java.util.List;

/**
 * @author DeYou
 * @date 2022/8/29
 */
public class BookDaoImpl extends BaseDao<Book> implements BookDao{
    @Override
    public List<Book> getBooks(Connection conn) {
        // 调用BaseDao中得到一个List的方法
        List<Book> beanList = null;
        // 写sql语句
        String sql = "select id,title,author,price,sales,stock,img_path imgPath from books";
        beanList = getBeanList(conn,sql);
        return beanList;
    }

    @Override
    public void saveBook(Connection conn, Book book) {
        // 写sql语句
        String sql = "insert into books(title,author,price,sales,stock,img_path) values(?,?,?,?,?,?)";
        // 调用BaseDao中通用的增删改的方法
        update(conn,sql, book.getTitle(), book.getAuthor(), book.getPrice(), book.getSales(), book.getStock(),book.getImgPath());
    }

    @Override
    public void deleteBookById(Connection conn, String bookId) {
        // 写sql语句
        String sql = "DELETE FROM books WHERE id = ?";
        // 调用BaseDao中通用增删改的方法
        update(conn,sql, bookId);
    }

    @Override
    public Book getBookById(Connection conn, String bookId) {
        // 调用BaseDao中获取一个对象的方法
        Book book = null;
        // 写sql语句
        String sql = "select id,title,author,price,sales,stock,img_path imgPath from books where id = ?";
        book = getBean(conn,sql, bookId);
        return book;
    }

    @Override
    public void updateBook(Connection conn, Book book) {
        // 写sql语句
        String sql = "update books set title = ? , author = ? , price = ? , sales = ? , stock = ? where id = ?";
        // 调用BaseDao中通用的增删改的方法
        update(conn,sql, book.getTitle(), book.getAuthor(), book.getPrice(), book.getSales(), book.getStock(), book.getId());
    }

    @Override
    public Page<Book> getPageBooks(Connection conn, Page<Book> page) {
        // 获取数据库中图书的总记录数
        String sql = "select count(*) from books";
        // 调用BaseDao中获取一个单一值的方法
        long totalRecord = (long) getValue(conn,sql);
        // 将总记录数设置都page对象中
        page.setTotalRecord((int) totalRecord);

        // 获取当前页中的记录存放的List
        String sql2 = "select id,title,author,price,sales,stock,img_path imgPath from books limit ?,?";
        // 调用BaseDao中获取一个集合的方法
        List<Book> beanList = getBeanList(conn,sql2, (page.getPageNo() - 1) * Page.PAGE_SIZE, Page.PAGE_SIZE);
        // 将这个List设置到page对象中
        page.setList(beanList);
        return page;
    }

    @Override
    public Page<Book> getPageBooksByPrice(Connection conn, Page<Book> page, double minPrice, double maxPrice) {
        // 获取数据库中图书的总记录数
        String sql = "select count(*) from books where price between ? and ?";
        // 调用BaseDao中获取一个单一值的方法
        long totalRecord = (long) getValue(conn,sql,minPrice,maxPrice);
        // 将总记录数设置都page对象中
        page.setTotalRecord((int) totalRecord);

        // 获取当前页中的记录存放的List
        String sql2 = "select id,title,author,price,sales,stock,img_path imgPath from books where price between ? and ? limit ?,?";
        // 调用BaseDao中获取一个集合的方法
        List<Book> beanList = getBeanList(conn,sql2, minPrice , maxPrice , (page.getPageNo() - 1) * Page.PAGE_SIZE, Page.PAGE_SIZE);
        // 将这个List设置到page对象中
        page.setList(beanList);

        return page;
    }
}