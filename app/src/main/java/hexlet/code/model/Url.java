package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Setter
@Getter
@ToString
public final class Url {
    private long id;
    @ToString.Include
    private String name;
    private Timestamp createdAt;

    public Url(String name) {
        this.name = name;
    }
}
