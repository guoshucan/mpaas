package ghost.framework.beans.annotation.tags;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.annotation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注释标签
 * @Date: 2020/2/7:13:18
 */
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotationTag {
    /**
     * 注释标签
     * @return
     */
    AnnotationTags value();

    /**
     * 注释标签的生命周期枚举
     * 主要是在加载与卸载的处理机制做区分
     * 关系 {@see ghost.framework.context.loader.ILoader} 接口的卸载与加载
     */
    enum AnnotationLifeCycle {
        /**
         * 加载
         * 对应 {@see ghost.framework.context.loader.ILoader::loader}
         */
        Loader,
        /**
         * 卸载
         * 对应 {@see ghost.framework.context.loader.ILoader::unloader}
         */
        UnLoader,
        /**
         * 未知
         * 对应 {@see ghost.framework.context.loader.ILoader::loader}
         * 对应 {@see ghost.framework.context.loader.ILoader::unloader}
         */
        Unknown;
    }
    /**
     * 注释标签枚举
     */
    enum AnnotationTags{
        /**
         * 包
         */
        Package,
        /**
         * 存储类型
         * 处理 {@see ghost.framework.core.event.annotation.container.IClassAnnotationEventFactoryContainer} 容器接口操作事件工厂的注释标签
         */
        StereoType,
        /**
         * 函数绑定
         * 处理 {@see ghost.framework.core.event.bean.container.IBeanEventFactoryContainer} 容器接口操作事件工厂的注释标签
         */
        Bean,
        /**
         * 容器
         */
        Container,
        /**
         * 操作绑定
         * 处理 {@see ghost.framework.core.event.bean.operating.container.IOperatingBeanEventFactoryContainer} 容器接口操作事件工厂的注释标签
         */
        OperatingBean,
        /**
         * 配置
         */
        Configuration,
        /**
         * 构建
         */
        Constructor,
        /**
         * 条件
         */
        Conditional,
        /**
         * 区域
         */
        Locale,
        /**
         *
         */
        Maven,
        /**
         * 依赖
         */
        Depend,
        /**
         * 注入
         */
        Injection,
        /**
         * 标签
         */
        Tag,
        /**
         * 约束
         */
        Constraints,
        /**
         * 调用
         */
        Invoke,
        /**
         * 验证
         */
        Valid,
        /**
         * 任务
         */
        Task,
        /**
         * 未知
         */
        Unknown;
    }
}
