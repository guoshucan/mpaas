package ghost.framework.beans.maven.annotation.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块注释
 * @Date: 22:43 2019-06-07
 */
@Target({
        ElementType.PACKAGE,
        ElementType.TYPE,
        ElementType.PARAMETER,
        ElementType.ANNOTATION_TYPE,
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.TYPE_USE,
        ElementType.CONSTRUCTOR
})
@Retention(RetentionPolicy.RUNTIME)
public @interface MavenModuleDependencys {
    /**
     * 依赖列表
     * @return
     */
    MavenModuleDependency[] value();
}
