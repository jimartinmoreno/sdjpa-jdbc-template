package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import guru.springframework.jdbc.domain.Book;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jt on 8/23/21.
 */
public class AuthorListMapper implements RowMapper<List<Author>> {
    @Override
    public List<Author> mapRow(ResultSet rs, int rowNum) throws SQLException {
        List<Author> authorList = new ArrayList<>();
        Author author = null;
        long id = 0L;
        do {
            //System.out.println(id + " == " + rs.getLong("id"));
            if (id != rs.getLong("id")) {
                author = new Author();
                authorList.add(author);
                id = rs.getLong("id");
                author.setId(id);
                author.setFirstName(rs.getString("first_name"));
                author.setLastName(rs.getString("last_name"));
                author.setBooks(new ArrayList<>());
                if (rs.getString("isbn") != null) {
                    // recupera el libro del primer resultado
                    author.getBooks().add(mapBook(rs));
                }
                //System.out.println("author - if: " + author);
            } else {
                //System.out.println("author - else: " + author);
                author.getBooks().add(mapBook(rs));
            }
        } while (rs.next());

        System.out.println(this.getClass() + "mapRow - authorList: " + authorList);
        return authorList;
    }

    private Book mapBook(ResultSet rs) throws SQLException {
        return new Book(rs.getLong("book_id"), rs.getString("title"), rs.getString("isbn"), rs.getString("publisher"), rs.getLong("id"));
    }
}
