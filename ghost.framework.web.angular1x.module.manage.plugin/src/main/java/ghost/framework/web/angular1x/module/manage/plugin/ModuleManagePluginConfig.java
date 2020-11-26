package ghost.framework.web.angular1x.module.manage.plugin;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.invoke.Loader;
import ghost.framework.beans.annotation.invoke.Unloader;
import ghost.framework.beans.annotation.module.Module;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.context.module.IModule;
import ghost.framework.data.hibernate.IHibernateBuilder;
import ghost.framework.web.angular1x.module.manage.plugin.entitys.*;
import ghost.framework.web.angular1x.module.manage.plugin.entitys.*;

/**
 * package: ghost.framework.web.angular1x.module.manage.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/4/21:10:21
 */
@Configuration
public class ModuleManagePluginConfig {
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
        //
        this.hibernateBuilder.add(MavenRepositoryEntity.class);
        //
        this.hibernateBuilder.add(RepositoryModuleEntity.class);
        this.hibernateBuilder.add(RepositoryModuleDataEntity.class);
        this.hibernateBuilder.add(RepositoryModuleAttributesEntity.class);
        //
        this.hibernateBuilder.add(ModuleEntity.class);
        this.hibernateBuilder.add(ModuleDataEntity.class);
        this.hibernateBuilder.add(ModuleAttributesEntity.class);
        //
        this.hibernateBuilder.add(PluginEntity.class);
        this.hibernateBuilder.add(PluginDataEntity.class);
        this.hibernateBuilder.add(PluginAttributesEntity.class);
        //重建数据源
        this.hibernateBuilder.rebuild();
    }

    /**
     * 卸载配置
     */
    @Unloader
    public void unloader() {
//        //添加数据源实体
//        this.hibernateBuilder.remove(ModuleEntity.class);
//        this.hibernateBuilder.remove(ModuleDataEntity.class);
//        this.hibernateBuilder.remove(ModuleAttributesEntity.class);
//        this.hibernateBuilder.remove(PluginEntity.class);
//        this.hibernateBuilder.remove(PluginDataEntity.class);
//        this.hibernateBuilder.remove(PluginAttributesEntity.class);
//        //重建数据源
//        this.hibernateBuilder.rebuild();
    }
}
