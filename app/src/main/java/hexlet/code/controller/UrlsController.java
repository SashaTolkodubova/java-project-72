package hexlet.code.controller;

import hexlet.code.dto.urls.RootPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collections;

public class UrlsController {

    public static void index(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        var page = new UrlsPage(urls);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("urls/index.jte", Collections.singletonMap("page", page));
    }

    public static void save(Context ctx) throws SQLException {
        try {
            var name = buildUrl(new URI(ctx.formParamAsClass("url", String.class).getOrDefault("")).toURL());

            if (UrlRepository.isExist(name)) {
                ctx.sessionAttribute("flash", "Страница уже существует");
                ctx.sessionAttribute("flashType", "info");
                ctx.redirect(NamedRoutes.urlsPath());
            } else {
                Url url = new Url(name);
                UrlRepository.save(url);
                ctx.sessionAttribute("flash", "Страница успешно добавлена");
                ctx.sessionAttribute("flashType", "success");
                ctx.redirect(NamedRoutes.urlsPath());
            }
        } catch (IllegalArgumentException | MalformedURLException | URISyntaxException e) {
            var page = new RootPage();
            page.setFlash("Некорректный URL");
            page.setFlashType("danger");
            ctx.status(400);
            ctx.render("root.jte", Collections.singletonMap("page", page));
        }
    }

    private static String buildUrl(URL url) {
        var protocol = url.getProtocol().isEmpty() ? "" : url.getProtocol();
        String host = url.getHost().isEmpty() ? "" : url.getHost();
        String port = url.getPort() == -1 ? "" : ":" + url.getPort();
        return String.format("%s://%s%s", protocol, host, port);
    }

}
