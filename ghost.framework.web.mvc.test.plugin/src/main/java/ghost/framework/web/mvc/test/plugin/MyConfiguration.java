package ghost.framework.web.mvc.test.plugin;

import ghost.framework.beans.annotation.bean.Bean;
import ghost.framework.beans.annotation.stereotype.Configuration;

/**
 * package: ghost.framework.web.mvc.test.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/1:18:05
 */
@Configuration
public class MyConfiguration {
    @Bean(value = "urlService")
    public UrlService urlService() {
        return () -> "domain.com/myapp";
    }

    public interface UrlService {
        String getApplicationUrl();
    }
}