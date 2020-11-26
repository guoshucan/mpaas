package ghost.framework.context.annotation;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * package: ghost.framework.core.annotation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/16:21:16
 */
public abstract class OutAnnotation<K extends Class<? extends Annotation>, V extends IAnnotationExecutionChain<K, V, L>, L extends List<V>>
        implements IOutAnnotation<K, V, L> {
    public OutAnnotation(Object domain, K key) {
        this.domain = domain;
        this.key = key;
    }

    private Object domain;

    @Override
    public Object getDomain() {
        return domain;
    }

    private K key;

    @Override
    public K getKey() {
        return key;
    }
}
