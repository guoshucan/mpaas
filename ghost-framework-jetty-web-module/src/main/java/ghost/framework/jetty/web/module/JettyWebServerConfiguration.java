//package ghost.framework.jetty.web.module;
//import ghost.framework.beans.annotation.Bean;
//import ghost.framework.beans.annotation.Order;
//import ghost.framework.beans.configuration.annotation.Configuration;
//import ghost.framework.web.module.http.request.HttpRequestMap;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:jetty配置
// * @Date: 11:25 2019/12/21
// */
//@Configuration(
//        //设置不Bean到模块容器中，此配置类只做运行过程
//        bean = false,
//        //注释双向绑定配置文件
//        type = {JettyProperties.class}
//        )
//public class JettyWebServerConfiguration {
//    private Logger log = LoggerFactory.getLogger(JettyWebServerConfiguration.class);
//    public JettyWebServerConfiguration(){
//        this.log.info(this.getClass().getName());
//    }
////    /**
////     * 如果容器未绑定时绑定它
////     *
////     * @return
////     */
////    @Order(0)
////    @Bean
////    public DefaultHttpAcceptHeaderMethodResolver DefaultHttpAcceptHeaderMethodResolver() {
////        return new DefaultHttpAcceptHeaderMethodResolver();
////    }
////
////    /**
////     * @return
////     */
////    @Order(1)
////    @Bean
////    public DefaultHttpAcceptHeaderIPAccessResolver DefaultHttpAcceptHeaderIPAccessResolver() {
////        return new DefaultHttpAcceptHeaderIPAccessResolver();
////    }
////
////    /**
////     * @return
////     */
////    @Order(2)
////    @Bean
////    public DefaultAbstractHttpInterceptorContainer HttpInterceptorContainer() {
////        return new DefaultAbstractHttpInterceptorContainer();
////    }
////
////    /**
////     * @return
////     */
////    @Order(3)
////    @Bean
////    public DefaultHttpResponseHeaderResolver DefaultHttpResponseHeaderResolver() {
////        return new DefaultHttpResponseHeaderResolver();
////    }
////
////    /**
////     * @return
////     */
////    @Order(4)
////    @Bean
////    public DefaultHttpResponseBodyResolver DefaultHttpResponseBodyResolver() {
////        return new DefaultHttpResponseBodyResolver();
////    }
////
////    /**
////     * @return
////     */
////    @Order(5)
////    @Bean
////    public DefaultHttpAcceptHeaderParameterResolver DefaultHttpAcceptHeaderParameterResolver() {
////        return new DefaultHttpAcceptHeaderParameterResolver();
////    }
////
////    /**
////     * @return
////     */
////    @Order(6)
////    @Bean
////    public DefaultHttpAcceptHeaderLocaleResolver DefaultHttpAcceptHeaderLocaleResolver() {
////        return new DefaultHttpAcceptHeaderLocaleResolver();
////    }
//
//    /**
//     * @return
//     */
//    @Order(7)
//    @Bean
//    public HttpRequestMap HttpRequestMap() {
//        return new HttpRequestMap();
//    }
//}