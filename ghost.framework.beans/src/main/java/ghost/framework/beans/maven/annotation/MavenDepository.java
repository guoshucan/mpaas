package ghost.framework.beans.maven.annotation;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.util.StringUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:maven仓库注释，格式id:null,username:null,password:null,type:default:url:http://maven.aliyun.com/nexus/content/groups/public/
 * @Date: 11:56 2019/11/30
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
public @interface MavenDepository {
    /**
     * 仓库id，默认为null
     * @return
     */
    String id() default "";

    /**
     * 仓库账号，默认为null
     * @return
     */
    String username() default "";

    /**
     * 仓库密码，默认为null
     * @return
     */
    String password() default "";

    /**
     * 仓库类型，默认为default
     * @return
     */
    String type() default "default";

    /**
     * 仓库地址
     * @return
     */
    @NotNull
    String url();
}