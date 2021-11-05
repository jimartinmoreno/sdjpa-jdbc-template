package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jt on 8/27/21.
 */
public class AuthorExtractor implements ResultSetExtractor<Author> {
    @Override
    public Author extractData(ResultSet rs) throws SQLException, DataAccessException {
        return new AuthorMapper().mapRow(rs, 0);
        // Alternativa a  crear una clase sería usar funciones lambda
        //        RowMapper<Author> myAuthorMapper = (rs_, row) ->{
        //            Author author = new Author();
        //            return author;
        //            .....
        //        };
        //        return myAuthorMapper.mapRow(rs, 0);
    }
}
