package ghost.framework.web.module.http.response;

import ghost.framework.web.context.bens.annotation.WebResourceResolver;
import ghost.framework.web.context.resource.AbstractWebResourceResolver;
import ghost.framework.web.context.io.IWebResource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * package: ghost.framework.web.module.http.response
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:默认响应资源解析器
 * @Date: 2020/3/14:23:59
 */
@WebResourceResolver
public final class DefaultWebResourceResolver extends AbstractWebResourceResolver {
    /**
     * 重写为默认解析器
     *
     * @return
     */
    @Override
    public boolean isDef() {
        return true;
    }

    @Override
    public void resolver(String path, HttpServletRequest request, HttpServletResponse response, IWebResource resource) throws IOException {
        super.resolver(path, request, response, resource);
    }
}
