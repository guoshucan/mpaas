package ghost.framework.web.angular1x.deploy.manage.plugin;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.invoke.Loader;
import ghost.framework.beans.annotation.invoke.Unloader;
import ghost.framework.beans.annotation.module.Module;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.context.module.IModule;
import ghost.framework.data.hibernate.IHibernateBuilder;
import ghost.framework.software.entity.*;

/**
 * package: ghost.framework.web.angular1x.deploy.manage.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/31:19:45
 */
@Configuration
public class DeployManageConfig {
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
        this.hibernateBuilder.getEntityList().add(SoftwareEntity.class);
        this.hibernateBuilder.getEntityList().add(SoftwareVersionEntity.class);
        this.hibernateBuilder.getEntityList().add(SoftwareInstallExecuteEntity.class);
        this.hibernateBuilder.getEntityList().add(SoftwareUninstallExecuteEntity.class);
        this.hibernateBuilder.getEntityList().add(SoftwareAfterInstallExecuteEntity.class);
        this.hibernateBuilder.getEntityList().add(SoftwareAfterUninstallExecuteEntity.class);
        this.hibernateBuilder.getEntityList().add(SoftwareBeforeInstallExecuteEntity.class);
        this.hibernateBuilder.getEntityList().add(SoftwareBeforeUninstallExecuteEntity.class);
        //重建数据源
        this.hibernateBuilder.rebuild();
    }

    /**
     * 卸载配置
     */
    @Unloader
    public void unloader() {
//        //添加数据源实体
//        this.hibernateBuilder.getEntityList().remove(SoftwareEntity.class);
//        this.hibernateBuilder.getEntityList().remove(SoftwareVersionEntity.class);
//        //重建数据源
//        this.hibernateBuilder.rebuild();
    }
}
