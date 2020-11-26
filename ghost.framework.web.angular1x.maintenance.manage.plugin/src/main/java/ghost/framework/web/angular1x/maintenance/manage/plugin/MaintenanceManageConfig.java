package ghost.framework.web.angular1x.maintenance.manage.plugin;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.invoke.Loader;
import ghost.framework.beans.annotation.invoke.Unloader;
import ghost.framework.beans.annotation.module.Module;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.context.module.IModule;
import ghost.framework.data.hibernate.IHibernateBuilder;
import ghost.framework.web.angular1x.maintenance.manage.plugin.entitys.TerminalAccountEntity;
import ghost.framework.web.angular1x.maintenance.manage.plugin.entitys.TerminalPemPrivateKeyEntity;

/**
 * package: ghost.framework.web.angular1x.maintenance.manage.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/4/26:16:38
 */
@Configuration
public class MaintenanceManageConfig {
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
        this.hibernateBuilder.add(TerminalAccountEntity.class);
        this.hibernateBuilder.add(TerminalPemPrivateKeyEntity.class);
        //重建数据源
        this.hibernateBuilder.rebuild();
    }

    /**
     * 卸载配置
     */
    @Unloader
    public void unloader() {
//        //添加数据源实体
//        this.hibernateBuilder.remove(TerminalAccountEntity.class);
//        //重建数据源
//        this.hibernateBuilder.rebuild();
    }
}