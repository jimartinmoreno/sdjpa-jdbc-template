package guru.springframework.jdbc;


import guru.springframework.jdbc.dao.AuthorDao;
import guru.springframework.jdbc.repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MySQLIntegrationTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorDao authorDao;

    @Test
    void testMySQL() {
        assertThat(authorDao).isNotNull();
        long countBefore = bookRepository.count();
        assertThat(countBefore).isPositive();
    }

}


