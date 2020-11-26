package ghost.framework.web.angular1x.top.message.plugin;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.invoke.Loader;
import ghost.framework.beans.annotation.module.Module;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.context.module.IModule;
import ghost.framework.data.hibernate.IHibernateBuilder;
import ghost.framework.web.top.message.entity.TopMessageEntity;

/**
 * package: ghost.framework.web.angular1x.top.message.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/7:23:05
 */
@Configuration
public class TopMessageConfig {
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
     * 加载顶部消息实体
     */
    @Loader
    public void loader() {
        this.hibernateBuilder.getEntityList().add(TopMessageEntity.class);
        //重建数据源
        this.hibernateBuilder.rebuild();
    }
}