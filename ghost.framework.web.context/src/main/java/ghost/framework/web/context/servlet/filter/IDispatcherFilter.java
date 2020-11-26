package ghost.framework.web.context.servlet.filter;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * package: ghost.framework.web.context.servlet.filter
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:调度过滤器接口
 * @Date: 2020/4/27:23:39
 */
public interface IDispatcherFilter extends Filter {
    /**
     * 获取过滤器执行链
     * @return
     */
    IFilterExecutionChain getFilterExecutionChain();
    ServletContext getContext();
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
//    void add(HttpSessionListener sessionListener);
//    void remove(HttpSessionListener sessionListener);
//    void add(ServletContextListener contextListener);
//    void remove(ServletContextListener contextListener);
}