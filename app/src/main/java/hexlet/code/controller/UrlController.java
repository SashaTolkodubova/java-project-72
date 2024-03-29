package hexlet.code.controller;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.repository.UrlRepository;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.sql.SQLException;
import java.util.Collections;

public class UrlController {
    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id).orElseThrow(() -> new NotFoundResponse("Url not found"));
        var page = new UrlPage(url);
        ctx.render("urls/show.jte", Collections.singletonMap("page", page));
    }
}
