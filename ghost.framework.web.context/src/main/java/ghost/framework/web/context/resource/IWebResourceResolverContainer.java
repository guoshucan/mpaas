package ghost.framework.web.context.resource;

import ghost.framework.context.io.resource.IResourceResolverContainer;
import ghost.framework.web.context.io.IWebResource;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

/**
 * package: ghost.framework.web.context.http.response.resource
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:资源解析容器接口
 * 作为各种网页的文件资源解析容器接口，比如解析html,css各自需要各自的解析器进行响应解析，
 * 解析器将继承{@link IWebResourceResolver#resolver(String, HttpServletRequest, HttpServletResponse, IWebResource)}接口函数实现解析响应内容
 * @Date: 2020/3/14:23:54
 */
public interface IWebResourceResolverContainer<T extends IWebResourceResolver> extends Collection<T>, IResourceResolverContainer<T> {
    /**
     * 判断类型是否可执行解析
     *
     * @param path
     * @param dispatcherType
     * @return
     */
    boolean isAllowed(String path, DispatcherType dispatcherType);

    /**
     * 获取禁止解析列表
     *
     * @return
     */
    Set<String> getDisallowed();

    /**
     * 获取允许解析列表
     *
     * @return
     */
    Set<String> getAllowed();

    /**
     * 解析http请求路径资源
     * @param path 请求路径
     * @param request 请求对象
     * @param response 响应对象
     * @param resource web资源接口
     * @throws IOException
     */
    void resolver(String path, HttpServletRequest request, HttpServletResponse response, IWebResource resource) throws IOException;
}