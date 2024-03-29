package hexlet.code.controller;

import hexlet.code.dto.urls.RootPage;
import io.javalin.http.Context;

import static io.javalin.rendering.template.TemplateUtil.model;

public class RootController {
    public static void index(Context ctx) {
        var page = new RootPage();
        ctx.render("root.jte", model("page", page));
    }
}
