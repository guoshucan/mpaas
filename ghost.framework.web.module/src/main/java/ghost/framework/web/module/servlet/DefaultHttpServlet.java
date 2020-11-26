//package ghost.framework.web.module.servlet;
//
//import ghost.framework.beans.annotation.container.BeanMapContainer;
//import ghost.framework.beans.annotation.stereotype.Component;
//import ghost.framework.context.io.IResource;
//import ghost.framework.web.context.http.HttpHeaders;
//import ghost.framework.web.context.http.HttpMethod;
//import ghost.framework.web.context.http.MediaType;
//import ghost.framework.web.context.io.IWebResource;
//import ghost.framework.web.context.io.WebIResourceLoader;
//import ghost.framework.web.context.servlet.context.IServletContainer;
//import ghost.framework.web.context.utils.DateUtils;
//import ghost.framework.web.context.utils.ETag;
//import ghost.framework.web.context.utils.ETagUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.log4j.Logger;
//
//import javax.servlet.DispatcherType;
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletConfig;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.text.SimpleDateFormat;
//import java.util.*;
///**
// * package: ghost.framework.web.module.servlet
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:默认内容处理
// * @Date: 2020/3/14:13:19
// */
//@BeanMapContainer(IServletContainer.class)
//@Component
//@WebServlet(loadOnStartup = 4, urlPatterns = {"/*"})
//public class DefaultHttpServlet extends HttpServlet {
//    private Logger logger = Logger.getLogger(DefaultHttpServlet.class);
//    public static final String DIRECTORY_LISTING = "directory-listing";
//    public static final String DEFAULT_ALLOWED = "default-allowed";
//    public static final String ALLOWED_EXTENSIONS = "allowed-extensions";
//    public static final String DISALLOWED_EXTENSIONS = "disallowed-extensions";
//    public static final String ALLOW_POST = "allow-post";
//    private static final Set<String> DEFAULT_ALLOWED_EXTENSIONS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("js", "css", "png", "jpg", "gif", "html", "htm", "txt", "pdf", "jpeg", "xml")));
//    /**
//     * 是否启用目录列表
//     */
//    private boolean directoryListingEnabled = false;
//    private boolean defaultAllowed = true;
//    private Set<String> allowed = DEFAULT_ALLOWED_EXTENSIONS;
//    private Set<String> disallowed = Collections.emptySet();
//    private boolean allowPost = false;
//    /**
//     * web全局资源加载器
//     */
//    private WebIResourceLoader resourceLoader;
//
//    @Override
//    public void init(ServletConfig config) throws ServletException {
//        super.init(config);
//        if (config.getInitParameter("isDefaultAllowed") != null) {
//            defaultAllowed = Boolean.valueOf(config.getInitParameter("isDefaultAllowed"));
//            allowed = new HashSet<>();
//            if (config.getInitParameter("Allowed") != null) {
//                allowed.addAll(Arrays.asList(config.getInitParameter("Allowed").split(",")));
//            }
//            disallowed = new HashSet<>();
//            if (config.getInitParameter("Disallowed") != null) {
//                disallowed.addAll(Arrays.asList(config.getInitParameter("Disallowed").split(",")));
//            }
//        }
//        if (config.getInitParameter(DEFAULT_ALLOWED) != null) {
//            defaultAllowed = Boolean.parseBoolean(config.getInitParameter(DEFAULT_ALLOWED));
//        }
//        if (config.getInitParameter(ALLOWED_EXTENSIONS) != null) {
//            String extensions = config.getInitParameter(ALLOWED_EXTENSIONS);
//            allowed = new HashSet<>(Arrays.asList(extensions.split(",")));
//        }
//        if (config.getInitParameter(DISALLOWED_EXTENSIONS) != null) {
//            String extensions = config.getInitParameter(DISALLOWED_EXTENSIONS);
//            disallowed = new HashSet<>(Arrays.asList(extensions.split(",")));
//        }
//        if (config.getInitParameter(ALLOW_POST) != null) {
//            allowPost = Boolean.parseBoolean(config.getInitParameter(ALLOW_POST));
//        }
//        String listings = config.getInitParameter(DIRECTORY_LISTING);
//        if (Boolean.valueOf(listings)) {
//            this.directoryListingEnabled = true;
//        }
//    }
//
//    @Override
//    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.service(req, resp);
//    }
//
//    /**
//     * get请求资源处理
//     *
//     * @param req
//     * @param resp
//     * @throws ServletException
//     * @throws IOException
//     */
//    @Override
//    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
//        //获取请求路径
//        String path = getPath(req);
//        //判断不允许的请求直接返回 SC_NOT_FOUND==404
//        if (!isAllowed(path, req.getDispatcherType())) {
//            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
//            return;
//        }
//        //获取请求资源路径
//        final IWebResource resource = resourceLoader.getResource(path);
//        if (resource == null) {
//            //
//            if (req.getDispatcherType() == DispatcherType.INCLUDE) {
//                //DispatcherType.INCLUDE 如果为本生处理引发错误
//                throw new FileNotFoundException(path);
//            } else {
//                //SC_NOT_FOUND==404
//                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
//            }
//            return;
//        } else if (resource.isDirectory()) {
//            //路径为目录处理
//            //判断是否启用目录列表返回
//            if (directoryListingEnabled) {
//                resp.setContentType("text/html");
//                StringBuilder output = this.renderDirectoryListing(req.getRequestURI(), resource);
//                resp.getWriter().write(output.toString());
//            } else {
//                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
//            }
//        } else {
//            if (path.endsWith("/")) {
//                //UNDERTOW-432
//                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
//                return;
//            }
//            serveFileBlocking(req, resp, resource);
//        }
//    }
//
//    /**
//     * 返回目录路径
//     *
//     * @param path
//     * @param resource
//     * @return
//     * @throws IOException
//     */
//    private static StringBuilder renderDirectoryListing(String path, IResource resource) throws IOException {
//        if (!path.endsWith("/")) {
//            path += "/";
//        }
//        StringBuilder builder = new StringBuilder();
//        builder.append("<html>\n<head>\n<script src='").append(path).append("?js'></script>\n")
//                .append("<link rel='stylesheet' type='text/css' href='").append(path).append("?css' />\n</head>\n");
//        builder.append("<body onresize='growit()' onload='growit()'>\n<table id='thetable'>\n<thead>\n");
//        builder.append("<tr><th class='loc' colspan='3'>Directory Listing - ").append(path).append("</th></tr>\n")
//                .append("<tr><th class='label offset'>Name</th><th class='label'>Last Modified</th><th class='label'>Size</th></tr>\n</thead>\n")
//                .append("<tfoot>\n<tr><th class=\"loc footer\" colspan=\"3\">Powered by Undertow</th></tr>\n</tfoot>\n<tbody>\n");
//
//        int state = 0;
//        String parent = null;
//        if (path.length() > 1) {
//            for (int i = path.length() - 1; i >= 0; i--) {
//                if (state == 1) {
//                    if (path.charAt(i) == '/') {
//                        state = 2;
//                    }
//                } else if (path.charAt(i) != '/') {
//                    if (state == 2) {
//                        parent = path.substring(0, i + 1);
//                        break;
//                    }
//                    state = 1;
//                }
//            }
//            if (parent == null) {
//                parent = "/";
//            }
//        }
//        int i = 0;
//        if (parent != null) {
//            i++;
//            builder.append("<tr class='odd'><td><a class='icon up' href='").append(parent).append("'>[..]</a></td><td>");
//            builder.append(format.format((resource.lastModified() == 0 ? new Date(0L) : new Date(resource.lastModified())))).append("</td><td>--</td></tr>\n");
//        }
//        for (IResource entry : resource.list()) {
//            builder.append("<tr class='").append((++i & 1) == 1 ? "odd" : "even").append("'><td><a class='icon ");
//            builder.append(entry.isDirectory() ? "dir" : "file");
//            builder.append("' href='").append(path).append(entry.getFilename()).append("'>").append(entry.getFilename()).append("</a></td><td>");
//            builder.append(format.format(entry.lastModified() == 0 ? new Date(0L) : new Date(entry.lastModified()))).append("</td><td>");
//            if (entry.isDirectory()) {
//                builder.append("--");
//            } else {
//                formatSize(builder, new Long(entry.contentLength()));
//            }
//            builder.append("</td></tr>\n");
//        }
//        builder.append("</tbody>\n</table>\n</body>\n</html>");
//        return builder;
//    }
//
//    private final static SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.US);
//
//    /**
//     * 格式化长度
//     *
//     * @param builder
//     * @param size
//     * @return
//     */
//    private static StringBuilder formatSize(StringBuilder builder, Long size) {
//        if (size == null) {
//            builder.append("???");
//            return builder;
//        }
//        int n = 1024 * 1024 * 1024;
//        int type = 0;
//        while (size < n && n >= 1024) {
//            n /= 1024;
//            type++;
//        }
//        long top = (size * 100) / n;
//        long bottom = top % 100;
//        top /= 100;
//        builder.append(top);
//        if (bottom > 0) {
//            builder.append(".").append(bottom / 10);
//            bottom %= 10;
//            if (bottom > 0) {
//                builder.append(bottom);
//            }
//
//        }
//        switch (type) {
//            case 0:
//                builder.append(" GB");
//                break;
//            case 1:
//                builder.append(" MB");
//                break;
//            case 2:
//                builder.append(" KB");
//                break;
//        }
//        return builder;
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        if (allowPost) {
//            doGet(req, resp);
//        } else {
//            /*
//             * Where a servlet has received a POST request we still require the capability to include static content.
//             */
//            switch (req.getDispatcherType()) {
//                case INCLUDE:
//                case FORWARD:
//                case ERROR:
//                    doGet(req, resp);
//                    break;
//                default:
//                    super.doPost(req, resp);
//            }
//        }
//    }
//
//    @Override
//    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        switch (req.getDispatcherType()) {
//            case INCLUDE:
//            case FORWARD:
//            case ERROR:
//                doGet(req, resp);
//                break;
//            default:
//                super.doPut(req, resp);
//        }
//    }
//
//    @Override
//    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        switch (req.getDispatcherType()) {
//            case INCLUDE:
//            case FORWARD:
//            case ERROR:
//                doGet(req, resp);
//                break;
//            default:
//                super.doDelete(req, resp);
//        }
//    }
//
//    @Override
//    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        switch (req.getDispatcherType()) {
//            case INCLUDE:
//            case FORWARD:
//            case ERROR:
//                doGet(req, resp);
//                break;
//            default:
//                super.doOptions(req, resp);
//        }
//    }
//
//    @Override
//    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        switch (req.getDispatcherType()) {
//            case INCLUDE:
//            case FORWARD:
//            case ERROR:
//                doGet(req, resp);
//                break;
//            default:
//                super.doTrace(req, resp);
//        }
//    }
//
//    /**
//     * 保存输出文件
//     *
//     * @param req      请求对象
//     * @param resp     响应对象
//     * @param resource 请求资源接口
//     * @throws IOException
//     */
//    private void serveFileBlocking(final HttpServletRequest req, final HttpServletResponse resp, final IWebResource resource) throws IOException {
//        //获取资源的etag对象
//        final ETag etag = resource.getETag();
//        //获取资源文件修改时间
//        final Date lastModified = new Date(resource.lastModified());
//        //处理文件缓存
//        if (!ETagUtils.handleIfMatch(req.getHeader(HttpHeaders.IF_MATCH), etag, false) ||
//                !DateUtils.handleIfUnmodifiedSince(req.getHeader(HttpHeaders.IF_UNMODIFIED_SINCE), lastModified)) {
//            resp.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
//            return;
//        }
//        if (!ETagUtils.handleIfNoneMatch(req.getHeader(HttpHeaders.IF_NONE_MATCH), etag, true) ||
//                !DateUtils.handleIfModifiedSince(req.getHeader(HttpHeaders.IF_MODIFIED_SINCE), lastModified)) {
//            if (req.getMethod().equals(HttpMethod.GET) || req.getMethod().equals(HttpMethod.HEAD)) {
//                resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
//            } else {
//                resp.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
//            }
//            return;
//        }
//        //判断处理响应内容类型
//        if (resp.getContentType() == null) {
//            if (!resource.isDirectory()) {
//                //获取资源内容类型
//                final String contentType = MediaType.valueOf(resource.getExtensionName()).getType();
//                if (contentType != null) {
//                    resp.setContentType(contentType);
//                } else {
//                    resp.setContentType("application/octet-stream");
//                }
//            }
//        }
//        //判断资源是否有修改时间
//        if (lastModified != null) {
//            //设置文件修改时间
//            resp.setHeader(HttpHeaders.LAST_MODIFIED, resource.getLastModifiedString());
//        }
//        if (etag != null) {
//            resp.setHeader(HttpHeaders.ETAG, etag.toString());
//        }
//        //处理断点续传支持
//        // set headers for the response
//        String headerKey = "Content-Disposition";
//        String headerValue = String.format("attachment; filename=\"%s\"", resource.getFilename());
//        resp.setHeader(headerKey, headerValue);
//        // 解析断点续传相关信息
//        resp.setHeader("Accept-Ranges", "bytes");
//        //资源文件大小
//        long downloadSize = resource.contentLength();
//        Long fromPos = new Long(0);
//        Long toPos = new Long(0);
//        if (req.getHeader("Range") == null) {
//            //一次性读取长度
//            resp.setHeader("Content-Length", downloadSize + "");
//        } else {
//            // 若客户端传来Range，说明之前下载了一部分，设置206状态(SC_PARTIAL_CONTENT)
//            resp.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
//            String range = req.getHeader("Range");
//            String bytes = range.replaceAll("bytes=", "");
//            String[] ary = bytes.split("-");
//            //读取断点开始位置
//            fromPos = Long.parseLong(ary[0]);
//            if (ary.length == 2) {
//                //读取断点结束位置
//                toPos = Long.parseLong(ary[1]);
//            }
//            //读取的段大小
//            int size;
//            //判断读取段大小
//            if (toPos > fromPos) {
//                size = (int) (toPos - fromPos);
//            } else {
//                size = (int) (downloadSize - fromPos);
//            }
//            //分段读取长度
//            resp.setHeader("Content-Length", size + "");
//            //重置分段大小
//            downloadSize = size;
//        }
//        //分块读取
//        OutputStream out = null;
//        try {
//            // 缓冲区大小
//            byte[] buffer = new byte[Long.valueOf(downloadSize < 2048 ? downloadSize : 2048).intValue()];
//            //读取数量
//            int num = 0;
//            if (fromPos > 0) {
//                num = fromPos.intValue();
//            }
//            //计数器
//            int count = 0;
//            out = resp.getOutputStream();
//            //循环读取流
//            while ((num = resource.getInputStream().read(buffer, num,
//                    //计算最后段大小
//                    ((downloadSize - count) >= buffer.length) ? buffer.length : new Long(downloadSize - count).intValue())) != -1) {
//                //写入流
//                out.write(buffer, 0, num);
//                //累加读取计数
//                count += num;
//                //处理最后一段，计算不满缓冲区的大小
//                if (count >= downloadSize) {
//                    break;
//                }
//            }
//            resp.flushBuffer();
//        } catch (IOException e) {
//            logger.info("数据被暂停或中断。");
//            throw e;
//        }
//    }
//
//    /**
//     * 获取请求路径
//     *
//     * @param request
//     * @return
//     */
//    private String getPath(final HttpServletRequest request) {
//        String servletPath;
//        String pathInfo;
//        //判断如果为转过来的获取转向路径信息
//        if (/*request.getDispatcherType() == DispatcherType.INCLUDE && */request.getAttribute(RequestDispatcher.INCLUDE_REQUEST_URI) != null) {
//            pathInfo = (String) request.getAttribute(RequestDispatcher.INCLUDE_PATH_INFO);
//            servletPath = (String) request.getAttribute(RequestDispatcher.INCLUDE_SERVLET_PATH);
//        } else {
//            pathInfo = request.getPathInfo();
//            servletPath = request.getServletPath();
//        }
//        String result = pathInfo;
//        if (result == null) {
//            //将本地路径转换为url路径
//            result = StringUtils.replaceAll(servletPath, "\\", "/");
//        }
//        if (StringUtils.isEmpty(result)) {
//            result = "/";
//        }
//        return result;
//    }
//
//    private boolean isAllowed(String path, DispatcherType dispatcherType) {
//        if (!path.isEmpty()) {
//            if (dispatcherType == DispatcherType.REQUEST) {
//                //WFLY-3543 allow the dispatcher to access stuff in web-inf and meta inf
//                if (path.startsWith("/META-INF") ||
//                        path.startsWith("META-INF") ||
//                        path.startsWith("/WEB-INF") ||
//                        path.startsWith("WEB-INF")) {
//                    return false;
//                }
//            }
//        }
//        if (defaultAllowed && disallowed.isEmpty()) {
//            return true;
//        }
//        int pos = path.lastIndexOf('/');
//        final String lastSegment;
//        if (pos == -1) {
//            lastSegment = path;
//        } else {
//            lastSegment = path.substring(pos + 1);
//        }
//        if (lastSegment.isEmpty()) {
//            return true;
//        }
//        int ext = lastSegment.lastIndexOf('.');
//        if (ext == -1) {
//            //no extension
//            return true;
//        }
//        final String extension = lastSegment.substring(ext + 1, lastSegment.length());
//        if (defaultAllowed) {
//            return !disallowed.contains(extension);
//        } else {
//            return allowed.contains(extension);
//        }
//    }
//}