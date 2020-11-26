package ghost.framework.data.beans.maven.annotation;
import ghost.framework.beans.configuration.annotation.ClassProperties;
import ghost.framework.beans.maven.annotation.MavenDependency;
import ghost.framework.data.configuration.jdbc.DataSourceProperties;
import java.lang.annotation.*;

/**
 * 数据源maven引用
 */
@Repeatable(DataSourceMavenDependencys.class)
@Target({
        ElementType.PACKAGE,
        ElementType.TYPE,
        ElementType.TYPE_USE,
        ElementType.ANNOTATION_TYPE,
})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSourceMavenDependency {
    /**
     * 绑定数组包
     * @return
     */
    MavenDependency[] dependencys();
    /**
     * 注入绑定类型列表
     * @return
     */
    String[] beanClass() default {};
    /**
     * 配置类
     * @return
     */
    ClassProperties[] beanPropertiesClass() default {};
    /**
     * 绑定数据源配置类
     * 默认使用 {@link ghost.framework.data.configuration.jdbc.DataSourceProperties} 类型
     * @return
     */
    Class<?> beanDataSourceProperties() default DataSourceProperties.class;
}