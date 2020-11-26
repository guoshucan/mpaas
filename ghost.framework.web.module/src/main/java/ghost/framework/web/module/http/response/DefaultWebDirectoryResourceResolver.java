package ghost.framework.web.module.http.response;

import ghost.framework.context.io.IResource;
import ghost.framework.web.context.bens.annotation.WebResourceResolver;
import ghost.framework.web.context.resource.IWebDirectoryResourceResolver;
import ghost.framework.web.context.io.IWebResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
/**
 * package: ghost.framework.web.module.http.response
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:默认响应目录资源解析器
 * @Date: 2020/3/15:10:03
 */
@WebResourceResolver
public final class DefaultWebDirectoryResourceResolver implements IWebDirectoryResourceResolver {
    private final Log logger = LogFactory.getLog(DefaultWebDirectoryResourceResolver.class);
    /**
     * 是否启用目录列表
     */
    private boolean directoryListingEnabled = false;

    @Override
    public boolean isDirectoryListingEnabled() {
        return directoryListingEnabled;
    }

    @Override
    public void setDirectoryListingEnabled(boolean directoryListingEnabled) {
        this.directoryListingEnabled = directoryListingEnabled;
    }

    @Override
    public boolean isResolver(String path, IWebResource resource) {
        return resource.isDirectory() && this.directoryListingEnabled;
    }

    @Override
    public void resolver(String path, HttpServletRequest request, HttpServletResponse response, IWebResource resource) throws IOException {
        if (directoryListingEnabled) {
            response.setContentType("text/html");
            StringBuilder output = this.renderDirectoryListing(request.getRequestURI(), resource);
            response.getWriter().write(output.toString());
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    /**
     * 返回目录路径
     *
     * @param path
     * @param resource
     * @return
     * @throws IOException
     */
    private static StringBuilder renderDirectoryListing(String path, IResource resource) throws IOException {
        if (!path.endsWith("/")) {
            path += "/";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("<html>\n<head>\n<script src='").append(path).append("?js'></script>\n")
                .append("<link rel='stylesheet' type='text/css' href='").append(path).append("?css' />\n</head>\n");
        builder.append("<body onresize='growit()' onload='growit()'>\n<table id='thetable'>\n<thead>\n");
        builder.append("<tr><th class='loc' colspan='3'>Directory Listing - ").append(path).append("</th></tr>\n")
                .append("<tr><th class='label offset'>Name</th><th class='label'>Last Modified</th><th class='label'>Size</th></tr>\n</thead>\n")
                .append("<tfoot>\n<tr><th class=\"loc footer\" colspan=\"3\">Powered by Undertow</th></tr>\n</tfoot>\n<tbody>\n");

        int state = 0;
        String parent = null;
        if (path.length() > 1) {
            for (int i = path.length() - 1; i >= 0; i--) {
                if (state == 1) {
                    if (path.charAt(i) == '/') {
                        state = 2;
                    }
                } else if (path.charAt(i) != '/') {
                    if (state == 2) {
                        parent = path.substring(0, i + 1);
                        break;
                    }
                    state = 1;
                }
            }
            if (parent == null) {
                parent = "/";
            }
        }
        int i = 0;
        if (parent != null) {
            i++;
            builder.append("<tr class='odd'><td><a class='icon up' href='").append(parent).append("'>[..]</a></td><td>");
            builder.append(format.format((resource.lastModified() == 0 ? new Date(0L) : new Date(resource.lastModified())))).append("</td><td>--</td></tr>\n");
        }
        for (IResource entry : resource.list()) {
            builder.append("<tr class='").append((++i & 1) == 1 ? "odd" : "even").append("'><td><a class='icon ");
            builder.append(entry.isDirectory() ? "dir" : "file");
            builder.append("' href='").append(path).append(entry.getFilename()).append("'>").append(entry.getFilename()).append("</a></td><td>");
            builder.append(format.format(entry.lastModified() == 0 ? new Date(0L) : new Date(entry.lastModified()))).append("</td><td>");
            if (entry.isDirectory()) {
                builder.append("--");
            } else {
                formatSize(builder, new Long(entry.contentLength()));
            }
            builder.append("</td></tr>\n");
        }
        builder.append("</tbody>\n</table>\n</body>\n</html>");
        return builder;
    }

    /**
     * 格式化长度
     *
     * @param builder
     * @param size
     * @return
     */
    private static StringBuilder formatSize(StringBuilder builder, Long size) {
        if (size == null) {
            builder.append("???");
            return builder;
        }
        int n = 1024 * 1024 * 1024;
        int type = 0;
        while (size < n && n >= 1024) {
            n /= 1024;
            type++;
        }
        long top = (size * 100) / n;
        long bottom = top % 100;
        top /= 100;
        builder.append(top);
        if (bottom > 0) {
            builder.append(".").append(bottom / 10);
            bottom %= 10;
            if (bottom > 0) {
                builder.append(bottom);
            }

        }
        switch (type) {
            case 0:
                builder.append(" GB");
                break;
            case 1:
                builder.append(" MB");
                break;
            case 2:
                builder.append(" KB");
                break;
        }
        return builder;
    }

    private final static SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.ENGLISH);
}
