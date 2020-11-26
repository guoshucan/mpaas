package ghost.framework.web.module.http.response;

import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.web.context.resource.IWebResourceResolver;
import ghost.framework.web.context.resource.IWebResourceResolverContainer;
import ghost.framework.web.context.io.IWebResource;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * package: ghost.framework.web.module.http.response.resource
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web资源解析器容器
 * @Date: 2020/3/14:23:56
 */
@Component
public class WebResourceResolverContainer
        extends AbstractCollection<IWebResourceResolver>
        implements IWebResourceResolverContainer<IWebResourceResolver> {
    /**
     * 默认可以解析列表
     */
    private static final Set<String> DEFAULT_ALLOWED_EXTENSIONS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("js", "css", "png", "jpg", "gif", "html", "htm", "txt", "pdf", "jpeg", "xml")));
    /**
     * 是否启用目录列表
     */
    private boolean directoryListingEnabled = false;

    private boolean defaultAllowed = true;
    /**
     * 可解析类型列表
     */
    private Set<String> allowed = DEFAULT_ALLOWED_EXTENSIONS;
    /**
     * 禁止解析类型列表
     */
    private Set<String> disallowed = Collections.emptySet();

    /**
     * 判断类型是否可执行解析
     *
     * @param path
     * @param dispatcherType
     * @return
     */
    @Override
    public boolean isAllowed(String path, DispatcherType dispatcherType) {
        if (!path.isEmpty()) {
            if (dispatcherType == DispatcherType.REQUEST) {
                //WFLY-3543 allow the dispatcher to access stuff in web-inf and meta inf
                if (path.startsWith("/META-INF") ||
                        path.startsWith("META-INF") ||
                        path.startsWith("/WEB-INF") ||
                        path.startsWith("WEB-INF")) {
                    return false;
                }
            }
        }
        if (defaultAllowed && disallowed.isEmpty()) {
            return true;
        }
        int pos = path.lastIndexOf('/');
        final String lastSegment;
        if (pos == -1) {
            lastSegment = path;
        } else {
            lastSegment = path.substring(pos + 1);
        }
        if (lastSegment.isEmpty()) {
            return true;
        }
        int ext = lastSegment.lastIndexOf('.');
        if (ext == -1) {
            //no extension
            return true;
        }
        final String extension = lastSegment.substring(ext + 1, lastSegment.length());
        if (defaultAllowed) {
            return !disallowed.contains(extension);
        } else {
            return allowed.contains(extension);
        }
    }

    @Override
    public Set<String> getDisallowed() {
        return disallowed;
    }

    @Override
    public Set<String> getAllowed() {
        return allowed;
    }

    @Override
    public void resolver(String path, HttpServletRequest request, HttpServletResponse response, IWebResource resource) throws IOException {
        for (IWebResourceResolver item : list) {
            if (!item.isDef() && item.isResolver(path, resource)) {
                item.resolver(path, request, response, resource);
                return;
            }
        }
        //使用默认解析器解析
        this.defaultResolver.resolver(path, request, response, resource);
    }

    @Override
    public Iterator<IWebResourceResolver> iterator() {
        return list.iterator();
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean add(IWebResourceResolver value) {
        //判断设置默认解析器
        if (value.isDef()) {
            this.defaultResolver = value;
        }
        return list.add(value);
    }

    /**
     * 默认解析器
     * 当未找到任何解析器后使用此默认解析器解析内容
     */
    private IWebResourceResolver defaultResolver;
    private List<IWebResourceResolver> list = new ArrayList<>();
}