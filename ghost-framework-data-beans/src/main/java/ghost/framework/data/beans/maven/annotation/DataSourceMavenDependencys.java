package ghost.framework.data.beans.maven.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 数据源maven引用
 */
@Target({
        ElementType.PACKAGE,
        ElementType.TYPE,
        ElementType.TYPE_USE,
        ElementType.ANNOTATION_TYPE,
})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSourceMavenDependencys {
    DataSourceMavenDependency[] value();
}
