package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import guru.springframework.jdbc.domain.Book;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by jt on 8/23/21.
 */
public class AuthorMapper implements RowMapper<Author> {
    @Override
    public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
        Author author = new Author();
        long id = 0L;
        if (rs.next()) {
            id = rs.getLong("id");
            // recupera el libro del primer resultado
            author.setId(id);
            author.setFirstName(rs.getString("first_name"));
            author.setLastName(rs.getString("last_name"));

            if (rs.getString("isbn") != null) {
                author.setBooks(new ArrayList<>());
                // recupera el libro del primer resultado
                author.getBooks().add(mapBook(rs));
                // recupera solo los libros de los siguientes resultados
                while (rs.next() && id == rs.getLong("id")) {
                    author.getBooks().add(mapBook(rs));
                }
            }
        } else {
            throw new EmptyResultDataAccessException("No record found", 0);
        }

        System.out.println(this.getClass() + "mapRow - author: " + author);
        return author;
    }

    private Book mapBook(ResultSet rs) throws SQLException {
        return new Book(rs.getLong("book_id"), rs.getString("title"), rs.getString("isbn"), rs.getString("publisher"), rs.getLong("id"));
    }
}
