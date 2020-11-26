package ghost.framework.web.angular1x.ssh.plugin;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.invoke.Loader;
import ghost.framework.beans.annotation.invoke.Unloader;
import ghost.framework.beans.annotation.module.Module;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.context.module.IModule;
import ghost.framework.data.hibernate.IHibernateBuilder;
import ghost.framework.util.IdGenerator;
import ghost.framework.web.angular1x.ssh.plugin.entity.*;

/**
 * package: ghost.framework.web.angular1x.ssh.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/25:9:38
 */
@Configuration
public class SshConfig {
    /**
     * 注入模块接口
     */
    @Module
    @Autowired
    private IModule module;
    /**
     * 注入Hibernate处理接口
     */
    @Application
    @Autowired
    private IHibernateBuilder hibernateBuilder;
    /**
     * 注入id生成接口
     */
    @Application
    @Autowired
    private IdGenerator generator;
    /**
     * 加载配置
     */
    @Loader
    public void loader() {
        //添加数据源实体
        this.hibernateBuilder.getEntityList().add(SshAccountEntity.class);
        this.hibernateBuilder.getEntityList().add(SshGroupEntity.class);
        this.hibernateBuilder.getEntityList().add(SshRegionEntity.class);
        this.hibernateBuilder.getEntityList().add(SshTypeEntity.class);
        this.hibernateBuilder.getEntityList().add(SshServerEntity.class);
        //重建数据源
        this.hibernateBuilder.rebuild();
    }

    /**
     * 卸载配置
     */
    @Unloader
    public void unloader() {
//        //添加数据源实体
//        this.hibernateBuilder.getEntityList().remove(SshServerRegionEntity.class);
//        this.hibernateBuilder.getEntityList().remove(SshServerGroupEntity.class);
//        this.hibernateBuilder.getEntityList().remove(SshServerEntity.class);
//        //重建数据源
//        this.hibernateBuilder.rebuild();
    }
}
