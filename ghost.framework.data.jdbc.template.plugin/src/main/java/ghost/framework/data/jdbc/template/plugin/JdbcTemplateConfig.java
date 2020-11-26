package ghost.framework.data.jdbc.template.plugin;

import ghost.framework.beans.annotation.Primary;
import ghost.framework.beans.annotation.bean.Bean;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.data.jdbc.template.JdbcTemplate;
import javax.sql.DataSource;

/**
 * package: ghost.framework.data.jdbc.template.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:配置jdbc模板
 * @Date: 2020/3/12:22:32
 */
@Configuration
public class JdbcTemplateConfig {
    private JdbcTemplate template;
    /**
     * 绑定模板
     * @param dataSource 从应用注入数据源
     * @return
     */
    @Primary
    @Bean
    public synchronized JdbcTemplate newJdbcTemplate(@Autowired DataSource dataSource) {
        if (template == null) {
            template = new JdbcTemplate(dataSource);
        }
        return template;
    }
}