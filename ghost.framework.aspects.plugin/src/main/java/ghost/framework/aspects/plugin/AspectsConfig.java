package ghost.framework.aspects.plugin;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.invoke.Loader;
import ghost.framework.beans.annotation.invoke.Unloader;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.context.annotation.IAnnotationRootExecutionChain;
import ghost.framework.context.application.IApplication;
import org.aspectj.lang.annotation.*;

/**
 * package: ghost.framework.aspects.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:aop配置
 * @Date: 2020/2/27:21:25
 */
@Configuration
public class AspectsConfig {
    /**
     * 注入应用接口
     */
    @Autowired
    private IApplication application;

    /**
     * 加载初始化aop插件资源
     */
    @Loader
    private void loader() {
        //获取注释链接口
        IAnnotationRootExecutionChain rootExecutionChain = this.application.getBean(IAnnotationRootExecutionChain.class);
        //注册注释
        rootExecutionChain.add(Aspect.class, AnnotationTag.AnnotationTags.Container);
        rootExecutionChain.add(Pointcut.class, AnnotationTag.AnnotationTags.Invoke);
        rootExecutionChain.add(Before.class, AnnotationTag.AnnotationTags.Invoke);
        rootExecutionChain.add(After.class, AnnotationTag.AnnotationTags.Invoke);
        rootExecutionChain.add(AfterReturning.class, AnnotationTag.AnnotationTags.Invoke);
        rootExecutionChain.add(AfterThrowing.class, AnnotationTag.AnnotationTags.Invoke);
        rootExecutionChain.add(Around.class, AnnotationTag.AnnotationTags.Invoke);
    }

    /**
     * 卸载插件清理aop插件包资源
     */
    @Unloader
    private void unloader() {
        //获取注释链接口
//        IAnnotationRootExecutionChain rootExecutionChain = this.application.getBean(IAnnotationRootExecutionChain.class);
//        rootExecutionChain.remove(Aspect.class);
//        rootExecutionChain.remove(Pointcut.class);
//        rootExecutionChain.remove(Before.class);
//        rootExecutionChain.remove(After.class);
//        rootExecutionChain.remove(AfterReturning.class);
//        rootExecutionChain.remove(AfterThrowing.class);
//        rootExecutionChain.remove(Around.class);
    }
}