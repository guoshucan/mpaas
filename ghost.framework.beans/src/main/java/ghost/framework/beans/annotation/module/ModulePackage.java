package ghost.framework.beans.annotation.module;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.beans.execute.LoadingMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * package: ghost.framework.beans.annotation.module
 * @Author: 郭树灿{guoshucan-pc}
 * @Description:模块包注释。
 * @Date: 16:12 2018/4/22
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PACKAGE})
@AnnotationTag(AnnotationTag.AnnotationTags.Package)
public @interface ModulePackage {
    /**
     * 是否为隔离模式模式
     * 如果为隔离模式时不同模块无法互相引用对象
     * 在 {@see ghost.framework.core.module.thread.ModuleThread::run} 过程按照下面模式添加引用包
     * 如果为 true 隔离模式将模块全部包添加入 {@see ghost.framework.context.module.IModuleClassLoader::addArtifactList}
     * 如果为 false 共享模式将模块全部包添加入 {@see ghost.framework.core.application.IApplicationClassLoader::addArtifactList}
     * @return
     */
    boolean isolation() default false;
    /**
     * 模块加载模式
     * 默认为注释加载模式
     * @return
     */
    LoadingMode mode() default LoadingMode.annotation;
    /**
     * 依赖类
     * @return
     */
    Class<?> [] depend() default {};
    /**
     * 模块name
     * 模块name将在应用的Bean绑定中作为绑定键
     * 如果模块未设置注释名称时，模块名称将使用模块版本格式作为模块名称
     * @return
     */
    String name() default "";
}