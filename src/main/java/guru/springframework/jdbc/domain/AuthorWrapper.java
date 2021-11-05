package guru.springframework.jdbc.domain;

import java.util.ArrayList;
import java.util.List;

public class AuthorWrapper {
    private List<Author> list = new ArrayList<>();

    public List<Author> getList() {
        return list;
    }

    public void setList(List<Author> list) {
        this.list = list;
    }
}
