package ghost.framework.web.angular1x.resource.container.manage.plugin;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.invoke.Loader;
import ghost.framework.beans.annotation.invoke.Unloader;
import ghost.framework.beans.annotation.module.Module;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.context.module.IModule;
import ghost.framework.data.hibernate.IHibernateBuilder;
import ghost.framework.resource.container.entity.*;

/**
 * package: ghost.framework.web.angular1x.admin.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用插件配置
 * @Date: 2020/3/22:16:33
 */
@Configuration
public class ResourceContainerManageConfig {
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
        this.hibernateBuilder.getEntityList().add(ResourceContainerEntity.class);
        this.hibernateBuilder.getEntityList().add(TencentCloudCosEntity.class);
        this.hibernateBuilder.getEntityList().add(AliyunOssEntity.class);
        this.hibernateBuilder.getEntityList().add(HdfsEntity.class);
        this.hibernateBuilder.getEntityList().add(FastDFSEntity.class);
        this.hibernateBuilder.getEntityList().add(MinioEntity.class);
        //重建数据源
        this.hibernateBuilder.rebuild();
    }

    /**
     * 卸载配置
     */
    @Unloader
    public void unloader() {
//        //添加数据源实体
//        this.hibernateBuilder.getEntityList().remove(ResourceContainerEntity.class);
//        this.hibernateBuilder.getEntityList().remove(TencentCloudCosEntity.class);
//        this.hibernateBuilder.getEntityList().remove(AliyunOssEntity.class);
//        this.hibernateBuilder.getEntityList().remove(HdfsEntity.class);
//        this.hibernateBuilder.getEntityList().remove(FastDFSEntity.class);
//        this.hibernateBuilder.getEntityList().remove(MinioEntity.class);
//        //重建数据源
//        this.hibernateBuilder.rebuild();
    }
}