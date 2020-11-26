package ghost.framework.web.angular1x.script.plugin;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.invoke.Loader;
import ghost.framework.beans.annotation.invoke.Unloader;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.data.hibernate.IHibernateBuilder;
import ghost.framework.web.angular1x.script.plugin.entity.*;

/**
 * package: ghost.framework.web.angular1x.ssh.software.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/8/16:1:15
 */
@Configuration
public class ScriptConfig {
    /**
     * 注入Hibernate处理接口
     */
    @Application
    @Autowired
    private IHibernateBuilder hibernateBuilder;

    /**
     * 加载配置
     */
    @Loader
    public void loader() {
        //添加数据源实体
        this.hibernateBuilder.getEntityList().add(CommandEntity.class);
        this.hibernateBuilder.getEntityList().add(JavaVersionEntity.class);
        this.hibernateBuilder.getEntityList().add(GoLangVersionEntity.class);
        this.hibernateBuilder.getEntityList().add(PythonVersionEntity.class);
        this.hibernateBuilder.getEntityList().add(ScriptEntity.class);
        //重建数据源
        this.hibernateBuilder.rebuild();
    }

    /**
     * 卸载配置
     */
    @Unloader
    public void unloader() {
//        this.hibernateBuilder.getEntityList().remove(SshScriptEntity.class);
        //重建数据源
//        this.hibernateBuilder.rebuild();
    }
}
