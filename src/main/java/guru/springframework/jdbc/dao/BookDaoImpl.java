package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import guru.springframework.jdbc.domain.Book;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by jt on 8/25/21.
 */
@Component
public class BookDaoImpl implements BookDao {
    private final JdbcTemplate jdbcTemplate;

    public BookDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Book getById(Long id) {
        String sql = "SELECT * FROM book where id = ?";
        return jdbcTemplate.queryForObject(sql, ((rs, i) -> new Book(rs.getLong(1), rs.getString(4), rs.getString(2), rs.getString(3), rs.getLong(5))), id);
    }

    @Override
    public Book findBookByTitle(String title) {
        String sql = "SELECT * FROM book where title = ?";
        return jdbcTemplate.queryForObject(sql, getBookMapper(), title);
    }

    @Override
    public Book saveNewBook(Book book) {
        jdbcTemplate.update("INSERT INTO book (isbn, publisher, title, author_id) VALUES (?, ?, ?, ?)",
                book.getIsbn(), book.getPublisher(), book.getTitle(), book.getAuthorId());

        Long createdId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        return this.getById(createdId);
    }

    @Override
    public Book updateBook(Book book) {
        jdbcTemplate.update("UPDATE book set isbn = ?, publisher = ?, title = ?, author_id = ? where id = ?",
                book.getIsbn(), book.getPublisher(), book.getTitle(), book.getAuthorId(), book.getId());

        return this.getById(book.getId());
    }

    @Override
    public void deleteBookById(Long id) {
        jdbcTemplate.update("DELETE from book where id = ?", id);
    }

    @Override
    public List<Book> findAll() {
        String sql = "select * from book";
        System.out.println("sql: " + sql);

        return jdbcTemplate.query(sql, DataClassRowMapper.newInstance(Book.class));
        //return jdbcTemplate.queryForStream(sql, getBookMapper()).toList();
        //return dbcTemplate.queryForStream(sql, DataClassRowMapper.newInstance(Author.class)).toList();
        //return jdbcTemplate.queryForStream(sql, getRowMapper()).toList();
        //return jdbcTemplate.queryForStream(sql, BeanPropertyRowMapper.newInstance(Book.class)).toList();

    }

    private RowMapper<Book> getBookMapper() {
        return new BookMapper();
    }
}
