package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.repository.BaseRepository;
import io.javalin.Javalin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class App {

    public static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }

    public static String readResourceFile(String fileName) throws IOException {
        var inputStream = App.class.getClassLoader().getResourceAsStream(fileName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    public static void main(String[] args) throws SQLException, IOException {
        var app = getApp();
        app.get("/", ctx -> ctx.result("Hello Hrusha"));
        app.start(getPort());
    }

    public static Javalin getApp() throws IOException, SQLException {

        var hikariConFIg = new HikariConfig();
        hikariConFIg.setJdbcUrl("jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");

        var dataSourse = new HikariDataSource(hikariConFIg);
        var sql = readResourceFile("schema.sql");

        try (var connection = dataSourse.getConnection();
        var statement = connection.createStatement()) {
            statement.execute(sql);
        }

        BaseRepository.dataSource = dataSourse;

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
        });
        return app;
    };
}
