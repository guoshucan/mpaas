package ghost.framework.web.angular1x.nginx.plugin;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.invoke.Loader;
import ghost.framework.beans.annotation.invoke.Unloader;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.data.hibernate.IHibernateBuilder;
import ghost.framework.data.hibernate.ISessionFactory;
import ghost.framework.web.angular1x.nginx.plugin.entity.*;
import ghost.framework.web.angular1x.nginx.plugin.hibernate.SshServerEntityEmptyInterceptor;

/**
 * package: ghost.framework.web.angular1x.nginx.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/8/21:17:51
 */
@Configuration
public class NginxConfig {
//    /**
//     * 注入模块接口
//     */
//    @Module
//    @Autowired
//    private IModule module;
    /**
     * 注入Hibernate处理接口
     */
    @Application
    @Autowired
    private IHibernateBuilder hibernateBuilder;
    @Application
    @Autowired
    private ISessionFactory sessionFactory;
    /**
     * 加载配置
     */
    @Loader
    public void loader() {

        //添加数据源实体
        this.hibernateBuilder.getEntityList().add(NginxBakEntity.class);
        this.hibernateBuilder.getEntityList().add(NginxBasicEntity.class);
        this.hibernateBuilder.getEntityList().add(NginxCertEntity.class);
        this.hibernateBuilder.getEntityList().add(NginxConfEntity.class);

        this.hibernateBuilder.getEntityList().add(NginxCreditEntity.class);
        this.hibernateBuilder.getEntityList().add(NginxHttpEntity.class);
        this.hibernateBuilder.getEntityList().add(NginxLocationEntity.class);
        this.hibernateBuilder.getEntityList().add(NginxLogEntity.class);

        this.hibernateBuilder.getEntityList().add(NginxLogInfoEntity.class);
        this.hibernateBuilder.getEntityList().add(NginxParamEntity.class);
        this.hibernateBuilder.getEntityList().add(NginxReverseProxyEntity.class);
        this.hibernateBuilder.getEntityList().add(NginxServerEntity.class);

        this.hibernateBuilder.getEntityList().add(NginxStaticWebEntity.class);
        this.hibernateBuilder.getEntityList().add(NginxStreamEntity.class);
        this.hibernateBuilder.getEntityList().add(NginxTemplateEntity.class);
        this.hibernateBuilder.getEntityList().add(NginxTemplateParamEntity.class);

        this.hibernateBuilder.getEntityList().add(NginxUpstreamEntity.class);
        this.hibernateBuilder.getEntityList().add(NginxUpstreamServerEntity.class);

        //重建数据源
        this.hibernateBuilder.rebuild();
        //添加拦截器
//        this.sessionFactory.getSessionFactoryOptions().getStatementInspector().
    }
    private SshServerEntityEmptyInterceptor sshServerEntityEmptyInterceptor = new SshServerEntityEmptyInterceptor();
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
