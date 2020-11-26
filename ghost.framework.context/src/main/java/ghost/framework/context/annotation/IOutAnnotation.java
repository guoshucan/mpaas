package ghost.framework.context.annotation;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * package: ghost.framework.context.annotation
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:返回注释tag对象接口
 * @Date: 2020/2/13:13:26
 */
public interface IOutAnnotation<K extends Class<? extends Annotation>, V extends IAnnotationExecutionChain<K, V, L>, L extends List<V>> {
    /**
     * 获取域对象
     *
     * @return
     */
    Object getDomain();

    /**
     * 获取注释键
     *
     * @return
     */
    K getKey();

    /**
     * 获取输出对象
     *
     * @return
     */
    default V getOut() {
        return null;
    }

    /**
     * 执行输出函数
     *
     * @param out
     * @return
     */
    boolean test(V out);
}