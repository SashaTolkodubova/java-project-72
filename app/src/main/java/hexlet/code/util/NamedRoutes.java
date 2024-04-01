package hexlet.code.util;

public class NamedRoutes {
    public static String rootPath() {
        return "/";
    }

    public static String urlsPath() {

        return "/urls";
    }

    public static String urlPath(String id) {

        return urlsPath() + "/" + id;
    }

    public static String urlPath(Long id) {
        return urlPath(String.valueOf(id));
    }

    public static String toCheckPath(Long id) {
        return "urls/" + id + "/checks";
    }

    public static String toCheckPath(String id) {
        return "urls/" + id + "/checks";
    }
}
