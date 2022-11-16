package ch07.test;

import ch07.dao.BookDao;
import ch07.dao.BookDaoImpl;
import ch07.entity.Book;
import org.junit.Test;
import utils.JDBCUtils;

import java.sql.Connection;

/**
 * @author DeYou
 * @date 2022/8/29
 */
public class TestBook {
    @Test
    public void testBook() throws Exception {
        BookDao bookDao = new BookDaoImpl();
        Book book = new Book();
        book.setAuthor( "Jones" );
        book.setTitle( "老人与海" );
        Connection connection = JDBCUtils.getConnection();
        bookDao.saveBook( connection,book );
        connection.close();
    }
}