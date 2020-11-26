package ghost.framework.context.utils;

/**
 * package: ghost.framework.context.utils
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/3/14:18:18
 */
public final class PathUtil {
    public static String connection(String a, String b) {
        if (a.indexOf(".") != -1) {
            a = a.replaceAll(".", "\\");
        }
        if (a.endsWith("\\") && !b.startsWith("\\")) {
            return a + b;
        }
        if (!a.endsWith("\\") && b.startsWith("\\")) {
            return a + b;
        }
        if (!a.endsWith("\\") && !b.startsWith("\\")) {
            return a + "\\" + b;
        }
        return a + b;
    }

    /**
     * The package separator character: {@code '.'}.
     */
    private static final char PACKAGE_SEPARATOR = '.';

    /**
     * The path separator character: {@code '/'}.
     */
    private static final char PATH_SEPARATOR = '/';

    public static String urlConnection(String a, String b) {
        //判断包路径替换为url路径
        if (a.indexOf(".") != -1) {
            a = a.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);
        }
        if (a.endsWith("/") && !b.startsWith("/")) {
            return a + b;
        }
        if (!a.endsWith("/") && b.startsWith("/")) {
            return a + b;
        }
        if (!a.endsWith("/") && !b.startsWith("/")) {
            return a + "/" + b;
        }
        return a + b;
    }
}