package ghost.framework.beans.annotation.application;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用模块依赖，如果模块注释此依赖是会吧依赖包安装进应用的类加载器
 * @Date: 18:50 2019/12/19
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
public @interface ApplicationDependencys {
    ApplicationDependency[] value();
}
