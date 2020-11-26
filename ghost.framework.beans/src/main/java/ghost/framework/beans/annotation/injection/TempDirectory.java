package ghost.framework.beans.annotation.injection;

import ghost.framework.beans.annotation.tags.AnnotationTag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.module.module.bind.annotation
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: 临时目录注释，只做模块内部注释，不对外
 * @Date: 2019-11-14:23:18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@AnnotationTag(AnnotationTag.AnnotationTags.Injection)
public @interface TempDirectory {
    /**
     * 增加路径
     *
     * @return
     */
    String[] increasePath() default {};

    /**
     * 拼接后缀函数名称
     * 后缀函数将拼接在 increasePath 参数后面
     *
     * @return
     */
    String[] suffixMethodName() default {};

    /**
     * 是否在进程退出时删除该注入的文件对象
     * 一半只在指定 {@link TempDirectory:suffix} 参数是从新创建 {@link java.io.File} 时才此参数设置为true才会有效
     *
     * @return
     */
    boolean deleteOnExit() default false;

    /**
     * 删除后创建目录
     *
     * @return
     */
    boolean deleteAfterCreation() default false;

    /**
     * 关联加载类参数
     *
     * @return
     */
    Class<?>[] depend() default {};
}