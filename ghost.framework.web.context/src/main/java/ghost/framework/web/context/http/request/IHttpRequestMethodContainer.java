package ghost.framework.web.context.http.request;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * package: ghost.framework.web.module.http.request
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:http请求函数容器接口
 * {@link javax.servlet.Filter#doFilter(ServletRequest, ServletResponse, FilterChain)}
 * {@link IHttpRequestMethodContainer#execute(HttpServletRequest, HttpServletResponse, FilterChain)}
 * {@link HttpRequestMethodPath} 作为容器的键
 * @Date: 13:54 2020/1/31
 */
public interface IHttpRequestMethodContainer extends Map<HttpRequestMethodPath, IHttpRequestMethod> {
    /**
     * 执行http请求函数处理
     * {@see IHttpRequestMethodFilter#doFilterInternal}
     * @param request  请求对象
     * @param response 响应对象
     * @param chain
     * @throws IOException
     * @throws ServletException
     * @return 返回是否已经处理，如果已经处理将不再循环过滤器
     */
    boolean execute(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException;
    /**
     * 获取请求函数列表
     *
     * @return
     */
    Map<HttpRequestMethodPath, IHttpRequestMethod> getSortedMap();
}