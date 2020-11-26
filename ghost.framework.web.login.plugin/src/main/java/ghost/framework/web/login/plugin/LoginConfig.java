package ghost.framework.web.login.plugin;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.invoke.Invoke;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.data.jdbc.template.JdbcTemplate;
import ghost.framework.data.jdbc.support.MetaDataAccessException;
import ghost.framework.context.utils.AssemblyUtil;

/**
 * package: ghost.framework.web.login.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/3/12:23:22
 */
@Configuration
public class LoginConfig {
    /**
     * 注入jdbc模板
     */
    @Application
    @Autowired
    private JdbcTemplate template;
    /**
     * 初始化数据库
     */
    @Invoke
    public synchronized void init() throws MetaDataAccessException {
        //初始化数据库
        //获取当前数据库类型数据库脚本文件
        if (!template.isTableExist("admin")) {
            //没有数据库初始化数据库表
            template.execute(AssemblyUtil.getResourceString(this.getClass(), "sql/schema-" + this.template.getProductName() + ".sql"));
        }
    }
}