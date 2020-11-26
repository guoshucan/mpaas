//package ghost.framework.web.module.servlet.filter;
//
//import ghost.framework.beans.annotation.conditional.ConditionalOnMissingClass;
//import ghost.framework.beans.annotation.container.BeanMapContainer;
//import ghost.framework.beans.annotation.order.Order;
//import ghost.framework.beans.annotation.stereotype.Component;
//import ghost.framework.web.context.servlet.context.IFilterContainer;
//import ghost.framework.web.context.servlet.filter.GenericFilter;
//import ghost.framework.web.context.servlet.filter.ISessionFilter;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * package: ghost.framework.web.module.servlet.filter
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 2020/2/23:22:46
// */
//@ConditionalOnMissingClass(FilterContainer.class)
//@BeanMapContainer(IFilterContainer.class)
//@Component
//@Order(2)//执行顺序，小为先
//@WebFilter(filterName = "SessionFilter", urlPatterns = "/*", displayName = "SessionFilter", description = "", dispatcherTypes = DispatcherType.REQUEST)
//public class SessionFilter extends GenericFilter implements ISessionFilter {
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        this.logger.info(this.filterConfig.getFilterName() + " before doFilter:" + request.toString() + "," + response.toString());
//        chain.doFilter(request, response);
//        this.logger.info(this.filterConfig.getFilterName() + " after doFilter:" + request.toString() + "," + response.toString());
//    }
//}