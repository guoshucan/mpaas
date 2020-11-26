//package ghost.framework.web.module.servlet;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.stereotype.Service;
//import ghost.framework.beans.annotation.container.BeanCollectionContainer;
//import ghost.framework.context.module.IModule;
//import ghost.framework.web.module.servlet.test.HttpServletTest;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.util.EnumSet;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * package: ghost.framework.web.module.servlet
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 2020/1/26:22:34
// */
//@Service
//@BeanCollectionContainer(ServletContextInitializerContainer.class)
//public class ServletContextInitializerMapping1 implements ServletContextInitializer {
//    @Autowired
//    private IModule module;
//    private Map<String, Filter> filterMap = new HashMap<>();
//
//    private Map<String, Servlet> servletMap = new HashMap<>();
//
//
//    private ServletContext servletContext;
//
//    @Override
//    public void onStartup(ServletContext servletContext) throws ServletException {
//        this.servletContext = servletContext;
////        this.servletContext.addListener(this.module.getBean(ServletContextListener.class));
//        this.servletContext.addFilter("test1", new Filter() {
//            @Override
//            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//                HttpServletRequest servletRequest = (HttpServletRequest) request;
//                System.out.println(servletRequest.getRequestURI());
//                chain.doFilter(request, response);
//            }
//
//            @Override
//            public void init(FilterConfig filterConfig) throws ServletException {
//
//            }
//
//            @Override
//            public void destroy() {
//
//            }
//        }).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE), false, "/*");
//////        IHttpFilterFactoryContainer filterFactoryContainer = this.module.getBean(IHttpFilterFactoryContainer.class);
////        IHttpServletFactoryContainer servletFactoryContainer = this.module.getBean(IHttpServletFactoryContainer.class);
////        List<IHttpServletFactory> list =  servletFactoryContainer.getHttpServletFactoryList();
////        for (IHttpServletFactory factory : list) {
//////            ServletRegistration.Dynamic dynamic = this.servletContext.addServlet(factory.getServletName(), factory);
//////                dynamic.setLoadOnStartup(factory.getLoadOnStartup());
////        }
//        this.servletContext.addServlet("HttpServletTest", new HttpServletTest());
//    }
//}