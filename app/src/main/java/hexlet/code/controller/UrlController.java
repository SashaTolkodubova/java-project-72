package hexlet.code.controller;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlChecksRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.Unirest;

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
            var result = Unirest.get(urlAdress).asString();
            var statusCode = result.getStatus();
            UrlCheck urlCheck = new UrlCheck(statusCode, null, null, null, id);
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
