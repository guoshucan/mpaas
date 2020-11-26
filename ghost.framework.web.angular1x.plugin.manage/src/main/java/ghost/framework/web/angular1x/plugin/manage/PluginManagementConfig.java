package ghost.framework.web.angular1x.plugin.management;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.invoke.Loader;
import ghost.framework.beans.annotation.invoke.Unloader;
import ghost.framework.beans.annotation.module.Module;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.context.module.IModule;
import ghost.framework.data.hibernate.IHibernateBuilder;
import ghost.framework.web.angular1x.plugin.management.entitys.PluginAttributesEntity;
import ghost.framework.web.angular1x.plugin.management.entitys.PluginEntity;

/**
 * package: ghost.framework.web.angular1x.plugin.management
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/4/19:10:08
 */
@Configuration
public class PluginManagementConfig {
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
     * 加载配置
     */
    @Loader
    public void loader() {
        //添加数据源实体
        this.hibernateBuilder.getEntityList().add(PluginEntity.class);
        this.hibernateBuilder.getEntityList().add(PluginAttributesEntity.class);
        //重建数据源
        this.hibernateBuilder.rebuild();
    }

    /**
     * 卸载配置
     */
    @Unloader
    public void unloader() {
//        this.module.removeBean(AdminGroupEntity.class);
//        this.module.removeBean(AdminGroupPermissionEntity.class);
//        this.module.removeBean(AdminEntity.class);

    }
}