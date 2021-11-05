package guru.springframework.jdbc.dao;


import guru.springframework.jdbc.domain.Book;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper implements RowMapper<Book> {

    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Book(rs.getLong(1), rs.getString(4), rs.getString(2), rs.getString(3), rs.getLong(5));
    }
}
