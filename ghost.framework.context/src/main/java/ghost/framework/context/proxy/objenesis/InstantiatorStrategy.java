package ghost.framework.context.proxy.objenesis;

/**
 * package: ghost.framework.context.proxy.objenesis
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/7/4:21:47
 */
public interface InstantiatorStrategy {
    <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> var1);
}
