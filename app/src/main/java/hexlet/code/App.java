package hexlet.code;

import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {
        var app = getApp();
        app.get("/", ctx -> ctx.result("Hello Hrusha"));
        app.start(7070);
    }

    public static Javalin getApp(){
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
        });
        return app;
    };
}
