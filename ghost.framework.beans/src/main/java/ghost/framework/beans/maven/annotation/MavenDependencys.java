package ghost.framework.beans.maven.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块依赖注释
 * @Date: 22:43 2019-06-07
 */
@Target({
        ElementType.TYPE,
        ElementType.PACKAGE,
        ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface MavenDependencys {
    /**
     * 依赖列表
     * @return
     */
    MavenDependency[] value();
}
