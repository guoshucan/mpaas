package ghost.framework.web.module.servlet.filter;
import ghost.framework.beans.utils.OrderAnnotationUtil;
import ghost.framework.web.context.servlet.filter.*;

import javax.servlet.*;
import java.io.IOException;
import java.util.*;

/**
 * package: ghost.framework.web.module.servlet.filter
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:过滤器执行链，作为全部自定义过滤器的执行核心
 * @Date: 2020/4/27:17:47
 */
final class FilterExecutionChain
        implements IFilterExecutionChain {
    /**
     * 过滤器执行链列表
     */
    private List<Filter> cxecutionChain = new ArrayList<>();

    /**
     * 初始化过滤器执行链，作为全部自定义过滤器的执行核心
     */
    public FilterExecutionChain() {
        //初始化过滤器地图
        map = new LinkedHashMap<>();
        //添加内容过滤器 0
        map.put(IBeforeFilter.class, new ArrayList<>());
        //添加字符串过滤器 1
        map.put(ICharacterEncodingFilter.class, new ArrayList<>());
        //添加跨域过滤器 2
        map.put(ICrossFilter.class, new ArrayList<>());
        //添加区域过滤器 3
        map.put(ILocaleContextFilter.class, new ArrayList<>());
        //添加Cookies过滤器 4
        map.put(ICookieFilter.class, new ArrayList<>());
        //添加会话过滤器 5
        map.put(ISessionFilter.class, new ArrayList<>());
        //添加权限过滤器 6
        map.put(IAuthorizationFilter.class, new ArrayList<>());
        //添加formData post数据过滤器 7
        map.put(IFormContentFilter.class, new ArrayList<>());
        //添加文件过滤器 8
        map.put(IMultipartFilter.class, new ArrayList<>());
        //添加请求过滤器 9
        map.put(IRequestMethodFilter.class, new ArrayList<>());
        //添加压缩过滤器 10
        map.put(IResponseGzipFilter.class, new ArrayList<>());
        //添加WebSocket过滤器 11
        map.put(IWsFilter.class, new ArrayList<>());
        //添加自定义过滤器 12
        map.put(IAfterFilter.class, new ArrayList<>());
    }

    private FilterConfig config;

    /**
     * 初始化执行链
     */
    private void initExecutionChain() {
        synchronized (cxecutionChain) {
            cxecutionChain.clear();
            synchronized (map) {
                for (Map.Entry<Class<? extends Filter>, List<Filter>> entry : map.entrySet()) {
                    synchronized (entry.getValue()) {
                        for (Filter filter : entry.getValue()) {
                            cxecutionChain.add(filter);
                        }
                    }
                }
            }
        }
    }

    private Class<? extends Filter> getFilterType(Filter filter) {
        if (filter instanceof IBeforeFilter) {
            return IBeforeFilter.class;
        }
        if (filter instanceof ICharacterEncodingFilter) {
            return ICharacterEncodingFilter.class;
        }
        if (filter instanceof ICrossFilter) {
            return ICrossFilter.class;
        }
        if (filter instanceof ILocaleContextFilter) {
            return ILocaleContextFilter.class;
        }
        if (filter instanceof ICookieFilter) {
            return ICookieFilter.class;
        }
        if (filter instanceof ISessionFilter) {
            return ISessionFilter.class;
        }
        if (filter instanceof IAuthorizationFilter) {
            return IAuthorizationFilter.class;
        }
        if (filter instanceof IFormContentFilter) {
            return IFormContentFilter.class;
        }
        if (filter instanceof IMultipartFilter) {
            return IMultipartFilter.class;
        }
        if (filter instanceof IRequestMethodFilter) {
            return IRequestMethodFilter.class;
        }
        if (filter instanceof IResponseGzipFilter) {
            return IResponseGzipFilter.class;
        }
        if (filter instanceof IWsFilter) {
            return IWsFilter.class;
        }
        if (filter instanceof IAfterFilter) {
            return IAfterFilter.class;
        }
        return Filter.class;
    }

    /**
     * 删除过滤器
     * @param filter
     */
    @Override
    public void remove(Filter filter) {
        Class<? extends Filter> c = getFilterType(filter);
        synchronized (map) {
            if (map.containsKey(c)) {
                List<Filter> list = map.get(c);
                synchronized (list) {
                    list.remove(filter);
                    filter.destroy();
                    OrderAnnotationUtil.listSort(list);
                }
                this.initExecutionChain();
                return;
            }
        }
    }

    /**
     * 添加过滤器
     * @param filter
     * @throws ServletException
     */
    @Override
    public void add(Filter filter) throws ServletException {
        Class<? extends Filter> c = getFilterType(filter);
        synchronized (map) {
            List<Filter> list = null;
            if (map.containsKey(c)) {
                list = map.get(c);
                synchronized (list) {
                    //判断当未配置初始化时不设置初始化配置
                    if (this.config != null) {
                        filter.init(this.config);
                    }
                    list.add(filter);
                    OrderAnnotationUtil.listSort(list);
                }
                this.initExecutionChain();
                return;
            }
            list = new ArrayList<>();
            list.add(filter);
            //判断当未配置初始化时不设置初始化配置
            if (this.config != null) {
                filter.init(this.config);
            }
            map.put(c, list);
            this.initExecutionChain();
        }
    }
    /**
     * 过滤器列表
     */
    private Map<Class<? extends Filter>, List<Filter>> map;

    /**
     * 获取过滤器地图
     * @return
     */
    @Override
    public Map<Class<? extends Filter>, List<Filter>> getMap() {
        return map;
    }

    /**
     * 枚举过滤器
     *
     * @return
     */
    @Override
    public Set<Map.Entry<Class<? extends Filter>, List<Filter>>> entrySet() {
        return map.entrySet();
    }

    /**
     * 向下执行过滤器链
     *
     * @param servletRequest
     * @param servletResponse
     * @param iterator
     * @param nextFilter
     * @throws IOException
     * @throws ServletException
     */
    private void next(ServletRequest servletRequest, ServletResponse servletResponse, Iterator<Filter> iterator, Filter nextFilter, FilterChain chain) throws IOException, ServletException {
        //判断下一个过滤器是否为空，为空表实为开始遍历过滤器执行链
        if (nextFilter == null) {
            if (iterator.hasNext()) {
                //获取下一个过滤器
                nextFilter = iterator.next();
            }
        }
        if (nextFilter == null) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }
        final Filter nFilter;
        //下次遍历过滤器执行链
        if (iterator.hasNext()) {
            nFilter = iterator.next();
        } else {
            nFilter = null;
        }
        if (nFilter == null) {
            nextFilter.doFilter(servletRequest, servletResponse, new FilterChain() {
                @Override
                public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
                    //执行回调度过滤器链接口
                    chain.doFilter(request, response);
                }
            });
        } else {
            nextFilter.doFilter(servletRequest, servletResponse, new FilterChain() {
                @Override
                public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
                    //执行下一个过滤器
                    next(request, response, iterator, nFilter, chain);
                }
            });
        }
    }

    /**
     * 执行过滤器链
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //复制遍历过滤器执行链
        Iterator<Filter> iterator = new ArrayList<>(cxecutionChain).iterator();
        //遍历下一个过滤器
        next(request, response, iterator, null, chain);
    }

    /**
     * 销毁过滤器
     */
    @Override
    public void destroy() {
        synchronized (cxecutionChain) {
            cxecutionChain.clear();
        }
        synchronized (map) {
            for (Map.Entry<Class<? extends Filter>, List<Filter>> entry : map.entrySet()) {
                synchronized (entry.getValue()) {
                    for (Filter filter : entry.getValue()) {
                        filter.destroy();
                    }
                }
            }
        }
    }

    /**
     * 过滤器配置
     * @param config
     * @throws ServletException
     */
    @Override
    public void setConfig(FilterConfig config) throws ServletException{
        this.config = config;
        synchronized (cxecutionChain) {
            for (Filter filter : cxecutionChain) {
                filter.init(config);
            }
        }
    }
}