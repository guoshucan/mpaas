package ghost.framework.web.mvc.nginx.ui.plugin;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.invoke.Loader;
import ghost.framework.beans.annotation.invoke.Unloader;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.data.hibernate.IHibernateBuilder;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.*;

/**
 * package: ghost.framework.web.mvc.nginx.ui.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/8/20:20:14
 */
@Configuration
public class NginxWebUIConfig {
//    /**
//     * 注入模块接口
//     */
//    @Module
//    @Autowired
//    private IModule module;
    /**
     * 注入Hibernate处理接口
     */
//    @Application
    @Autowired
    private IHibernateBuilder hibernateBuilder;
//    /**
//     * 注入id生成接口
//     */
//    @Application
//    @Autowired
//    private IdGenerator generator;
    /**
     * 加载配置
     */
    @Loader
    public void loader() {
        //添加数据源实体
        this.hibernateBuilder.getEntityList().add(Basic.class);
        this.hibernateBuilder.getEntityList().add(Cert.class);
        this.hibernateBuilder.getEntityList().add(Credit.class);
        this.hibernateBuilder.getEntityList().add(Http.class);
        this.hibernateBuilder.getEntityList().add(Location.class);
        this.hibernateBuilder.getEntityList().add(Log.class);
        this.hibernateBuilder.getEntityList().add(LogInfo.class);
        this.hibernateBuilder.getEntityList().add(Param.class);
        this.hibernateBuilder.getEntityList().add(Remote.class);
        this.hibernateBuilder.getEntityList().add(Server.class);
        this.hibernateBuilder.getEntityList().add(Setting.class);
        this.hibernateBuilder.getEntityList().add(Stream.class);
        this.hibernateBuilder.getEntityList().add(Template.class);
        this.hibernateBuilder.getEntityList().add(TemplateParam.class);
        this.hibernateBuilder.getEntityList().add(Upstream.class);
        this.hibernateBuilder.getEntityList().add(UpstreamServer.class);
        //重建数据源
        this.hibernateBuilder.rebuild();
    }

    /**
     * 卸载配置
     */
    @Unloader
    public void unloader() {
//        this.hibernateBuilder.getEntityList().remove(Basic.class);
//        this.hibernateBuilder.getEntityList().remove(Cert.class);
//        this.hibernateBuilder.getEntityList().remove(Credit.class);
//        this.hibernateBuilder.getEntityList().remove(Http.class);
//        this.hibernateBuilder.getEntityList().remove(Location.class);
//        this.hibernateBuilder.getEntityList().remove(Log.class);
//        this.hibernateBuilder.getEntityList().remove(LogInfo.class);
//        this.hibernateBuilder.getEntityList().remove(Param.class);
//        this.hibernateBuilder.getEntityList().remove(Remote.class);
//        this.hibernateBuilder.getEntityList().remove(Server.class);
//        this.hibernateBuilder.getEntityList().remove(Setting.class);
//        this.hibernateBuilder.getEntityList().remove(Stream.class);
//        this.hibernateBuilder.getEntityList().remove(Template.class);
//        this.hibernateBuilder.getEntityList().remove(TemplateParam.class);
//        this.hibernateBuilder.getEntityList().remove(Upstream.class);
//        this.hibernateBuilder.getEntityList().remove(UpstreamServer.class);
        //重建数据源
//        this.hibernateBuilder.rebuild();
    }
}
