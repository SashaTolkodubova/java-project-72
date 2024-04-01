package hexlet.code.model;

import hexlet.code.repository.UrlChecksRepository;
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

    public UrlCheck getlastCheck() {
        UrlCheck lastCheck;
        try {
            lastCheck = UrlChecksRepository.getLastCheck(id);
        } catch (Exception e) {
            lastCheck = null;
        }
        return lastCheck;
    }
}
