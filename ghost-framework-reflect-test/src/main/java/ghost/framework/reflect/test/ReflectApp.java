package ghost.framework.reflect.test;

import ghost.framework.module.configuration.annotations.ConfigurationProperties;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 0:30 2019/6/4
 */
@ConfigurationProperties(order = 1)
public class ReflectApp extends ReflectApp0{
    public static void main(String ... strings){
        ConfigurationProperties[] properties = ReflectApp.class.getAnnotationsByType(ConfigurationProperties.class);
        System.out.println(properties.toString());
    }
}
