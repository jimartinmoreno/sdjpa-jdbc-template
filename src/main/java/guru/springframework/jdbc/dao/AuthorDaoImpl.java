package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import guru.springframework.jdbc.domain.AuthorWrapper;
import guru.springframework.jdbc.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by jt on 8/22/21.
 */
@Component
public class AuthorDaoImpl implements AuthorDao {

    private final JdbcTemplate jdbcTemplate;

    //@Autowired
    public AuthorDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static Author extractData(ResultSet rs) throws SQLException {
        return new AuthorMapper().mapRow(rs, 0);
    }

    @Override
    public Author getById(Long id) {
        String sql = "select author.id as id, first_name, last_name, book.id as book_id, book.isbn, book.publisher, book.title from author " +
                "left outer join book on author.id = book.author_id where author.id = ?";
        System.out.println("sql: " + sql);
        System.out.println("id: " + id);

        return jdbcTemplate.query(sql, new AuthorExtractor(), id);
        // return jdbcTemplate.queryForObject(sql, getRowMapper(), id);

        ////////////////////////////////////
        // Alternativas con funciones Lambda
        ////////////////////////////////////
        // ResultSetExtractor<Author> myResultSetExtractor = myResultSet -> new AuthorMapper().mapRow(myResultSet, 0);
        // return jdbcTemplate.query(sql, myResultSetExtractor, id);
        // return jdbcTemplate.query(sql, AuthorDaoImpl::extractData, id);
        // return jdbcTemplate.query(sql,
        //      myResultSet -> {
        //          return new AuthorMapper().mapRow(myResultSet, 0);
        //      },
        //      id);
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        String sql = "select author.id as id, first_name, last_name, book.id as book_id, book.isbn, book.publisher, book.title from author\n" +
                "left outer join book on author.id = book.author_id where author.first_name = ? and author.last_name = ?";
        System.out.println("sql: " + sql);
        return jdbcTemplate.queryForObject(sql, getRowMapper(), firstName, lastName);
        //return jdbcTemplate.queryForObject(sql, new Object[]{firstName, lastName}, getRowMapper());
    }

    @Override
    public Author saveNewAuthor(Author author) {
        jdbcTemplate.update("INSERT INTO author (first_name, last_name) VALUES (?, ?)",
                author.getFirstName(), author.getLastName());

        Long createdId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        System.out.println("createdId: " + createdId);

        return this.getById(createdId);
    }

    @Override
    public Author updateAuthor(Author author) {

        jdbcTemplate.update("UPDATE author SET first_name = ?, last_name = ? WHERE id = ?",
                author.getFirstName(), author.getLastName(), author.getId());

        return this.getById(author.getId());
    }

    @Override
    public void deleteAuthorById(Long id) {
        System.out.println("the number of rows deleted: " + jdbcTemplate.update("DELETE FROM author WHERE id = ?", id));
    }

    @Override
    public List<Author> findAll() {

        String sql = "select author.id as id, first_name, last_name, book.id as book_id, book.isbn, book.publisher, book.title from author " +
                "left outer join book on author.id = book.author_id";
        System.out.println("sql: " + sql);

        // List<List<Author>> auListList = jdbcTemplate.queryForStream(sql, getRowListMapper()).toList();
        // System.out.println("auListList (1): " + auListList.get(0));
        // List<List<Author>> auListList2 = jdbcTemplate.query(sql, getRowListMapper());
        // System.out.println("auListList (2): " + auListList2.get(0));
        return jdbcTemplate.queryForObject(sql, getRowListMapper());

    }

    private RowMapper<Author> getRowMapper() {
        return new AuthorMapper();
    }

    private RowMapper<List<Author>> getRowListMapper() {
        return new AuthorListMapper();
    }

    // Se puede usar tambien este mapper
    private RowMapper<AuthorWrapper> getWrapperMapper() {
        return new AuthorWrapperMapper();
    }

}












