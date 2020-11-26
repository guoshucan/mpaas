package ghost.framework.beans.annotation.module;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.util.StringUtil;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * package: ghost.framework.beans.annotation.module
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块注释
 * @Date: 12:44 2020/1/12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.PACKAGE,
        ElementType.TYPE,
        ElementType.METHOD,
        ElementType.ANNOTATION_TYPE,
        ElementType.FIELD,
        ElementType.TYPE_USE,
        ElementType.TYPE_PARAMETER,
        ElementType.LOCAL_VARIABLE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER
})
@AnnotationTag(AnnotationTag.AnnotationTags.Tag)
public @interface Module {
    /**
     * 模块名称
     * 如果未指定名称时，将 {@rz ghost.framework.core.module.thread.ModuleThreadLocal} 在模块线程上线文获取线程上线文的模块进行处理
     * @return
     */
    String value() default "";
//    /**
//     * 是否为委派注入模式
//     * 如果为委派注入模式侧在当前层级找不到往上一层获取绑定注入对象
//     * 只有在module属性为true时此配置true才有效
//     * @return
//     */
//    boolean delegation() default false;
}