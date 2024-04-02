package hexlet.code.controller;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlChecksRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.sql.SQLException;
import java.util.Collections;

public class UrlController {
    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id).orElseThrow(() -> new NotFoundResponse("Url not found"));
        var page = new UrlPage(url, UrlChecksRepository.getUrlChecks(id));
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("urls/show.jte", Collections.singletonMap("page", page));
    }

    public static void checkUrl(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id).orElseThrow(() -> new NotFoundResponse("Url not found"));
        var urlAdress = url.getName();
        try {
            HttpResponse<String> response = Unirest.get(urlAdress).asString();
            Document doc = Jsoup.parse(response.getBody());
            var statusCode = response.getStatus();
            String title = doc.title();
            String h1 = doc.selectFirst("h1") == null ? null : doc.selectFirst("h1").text();
            Element elementDescription = doc.selectFirst("meta[name=description]");
            String description = elementDescription == null ? null : elementDescription.attr("content");

            UrlCheck urlCheck = new UrlCheck(statusCode, title, h1, description, id);
            UrlChecksRepository.save(urlCheck);
            ctx.sessionAttribute("flash", "Страница успешно проверена");
            ctx.sessionAttribute("flashType", "success");
            ctx.redirect(NamedRoutes.urlPath(id));
        } catch (Exception e) {
            ctx.sessionAttribute("flash", e.getMessage());
            ctx.sessionAttribute("flashType", "danger");
            ctx.status(400);
            ctx.redirect(NamedRoutes.urlPath(id));
        }
    }
}
