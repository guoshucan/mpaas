package ghost.framework.web.context.servlet.filter;

import ghost.framework.web.context.cors.CorsConfigurationSource;
import ghost.framework.web.context.cors.CorsProcessor;

import javax.servlet.Filter;

/**
 * package: ghost.framework.web.module.servlet.filter
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:跨域过滤器接口
 * @Date: 2020/2/15:13:42
 */
public interface ICrossFilter extends Filter {
    CorsConfigurationSource getConfigSource();
    void setConfigSource(CorsConfigurationSource configSource);
    void setCorsProcessor(CorsProcessor processor);
    CorsProcessor getProcessor();
}