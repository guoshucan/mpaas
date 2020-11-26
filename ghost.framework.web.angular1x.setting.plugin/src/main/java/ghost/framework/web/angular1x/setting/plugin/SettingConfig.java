package ghost.framework.web.angular1x.setting.plugin;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.invoke.Loader;
import ghost.framework.beans.annotation.invoke.Unloader;
import ghost.framework.beans.annotation.module.Module;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.context.module.IModule;
import ghost.framework.data.hibernate.IHibernateBuilder;
import ghost.framework.web.setting.entity.WebSettingEntity;
import ghost.framework.web.setting.entity.WebSettingLogoEntity;

/**
 * package: ghost.framework.web.angular1x.setting.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/8:14:22
 */
@Configuration
public class SettingConfig {
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

    @Loader
    public void loader() {
        this.hibernateBuilder.getEntityList().add(WebSettingEntity.class);
        this.hibernateBuilder.getEntityList().add(WebSettingLogoEntity.class);
        //重建数据源
        this.hibernateBuilder.rebuild();
    }

    @Unloader
    public void unloader() {
//        this.hibernateBuilder.getEntityList().remove(WebSettingEntity.class);
//        //重建数据源
//        this.hibernateBuilder.rebuild();
    }
}