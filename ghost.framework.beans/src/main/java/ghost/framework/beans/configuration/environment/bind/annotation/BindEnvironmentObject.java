package ghost.framework.beans.configuration.environment.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:对象声明绑定env对象注释，主要在配置类声明跨一个或多个配置参数节点时注释
 * 此注释的类型都必须注释 {@link BindEnvironmentProperties} 注释才有效
 * @Date: 18:27 2019/12/23
 */
@Target({
        ElementType.FIELD, ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface BindEnvironmentObject {
    /**
     * 配置前缀
     * 对象是前缀参数
     * 比如声明对象为ghost.framework.xxx，此时前缀为ghost.framework
     * ghost.framework.xxx.a与ghost.framework.xxx.b就属于此声明对象的下级声明属性
     * @return
     */
    String prefix();
}
