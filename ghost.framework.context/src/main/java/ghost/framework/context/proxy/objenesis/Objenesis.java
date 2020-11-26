package ghost.framework.context.proxy.objenesis;

/**
 * package: ghost.framework.context.proxy.objenesis
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/7/4:21:46
 */
public interface Objenesis {
    <T> T newInstance(Class<T> var1);
    <T> ObjectInstantiator<T> getInstantiatorOf(Class<T> var1);
}
