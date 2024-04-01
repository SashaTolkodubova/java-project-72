package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;


public class AppTest {

    private Javalin app;
    private static MockWebServer mockWebServer;

    @BeforeAll
    public static void mockStart() throws IOException {
        mockWebServer = new MockWebServer();
        var mockResponse = new MockResponse().setBody(App.readResourceFile("fixtures/rootPage.html"));
        mockWebServer.enqueue(mockResponse);
        mockWebServer.start();
    }

    @AfterAll
    public static void mockStop() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    public final void setUp() throws Exception {
        app = App.getApp();
    }

    @Test
    public void testRootPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.rootPath());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Сайты");
        });
    }

    @Test
    public void testSaveUrlAndUrlPage() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https%3A%2F%2Fwww.example.com";
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            var response1 = client.get(NamedRoutes.urlPath("1"));
            Assertions.assertTrue(UrlRepository.isExist("https://www.example.com"));
            assertThat(response1.code()).isEqualTo(200);
            assertThat(response1.body().string()).contains("https://www.example.com");
        });
    }

    @Test
    public void testUrlCheck() throws SQLException {
        Url url = new Url(mockWebServer.url("/").toString());
        UrlRepository.save(url);
        Url savedUrl = UrlRepository.findByName(mockWebServer.url("/").toString());
        JavalinTest.test(app, (server, client) -> {
            var response = client.post("/" + NamedRoutes.toCheckPath(savedUrl.getId()));
            assertThat(response.code()).isEqualTo(200);
        });
    }
}
