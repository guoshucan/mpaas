package ghost.framework.beans.annotation.module;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.util.StringUtil;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * package: ghost.framework.beans.annotation.module
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块注释，指定模块版本信息的注释，如果此版本模块没在应用的模块容器里面时自动到maven仓库下载并安装到应用模块容器中
 * @Date: 15:58 2020/1/17
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
public @interface ModuleArtifact {
    /**
     * 模块组注释
     * @return
     */
    String groupId();

    /**
     * 模块定位
     * @return
     */
    String artifactId();

    /**
     * 模块版本
     * 如果未指定版本侧使用最新版本
     * @return
     */
    String version() default "";
}