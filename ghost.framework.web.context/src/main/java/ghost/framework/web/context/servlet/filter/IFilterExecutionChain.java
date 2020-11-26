package ghost.framework.web.context.servlet.filter;

import javax.servlet.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * package: ghost.framework.web.module.servlet.filter
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/4/27:17:50
 */
public interface IFilterExecutionChain {
    void setConfig(FilterConfig config) throws ServletException;
    Map<Class<? extends Filter>, List<Filter>> getMap();

    /**
     * 枚举过滤器
     *
     * @return
     */
    Set<Map.Entry<Class<? extends Filter>, List<Filter>>> entrySet();

    /**
     * 执行过滤器链
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException;
    /**
     * 销毁过滤器
     */
    void destroy();
    /**
     * 添加过滤器
     *
     * @param filter
     * @throws ServletException
     */
    void add(Filter filter) throws ServletException;
    /**
     * 删除过滤器
     *
     * @param filter
     */
    void remove(Filter filter);
}