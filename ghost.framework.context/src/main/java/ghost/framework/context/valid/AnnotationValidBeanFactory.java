package ghost.framework.context.valid;

import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.jsr303.valid.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注释验证工厂接口
 * @Date: 2020/2/29:13:49
 */
public interface AnnotationValidBeanFactory {
    /**
     * 获取注释类型
     *
     * @return
     */
    Class<? extends Annotation> getAnnotationClass();

    /**
     * 获取连接验证注释类型列表
     * 表示此验证后连续验证的其它数组验证注释类型
     * @return
     */
    default Class<? extends Annotation>[] link() {
        return null;
    }
}