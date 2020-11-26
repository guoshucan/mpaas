package ghost.framework.context.base;

/**
 * package: ghost.framework.core.base
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:对象注入接口
 * @Date: 2019/12/11:22:13
 */
public interface IObjectInjection {
    /**
     * 注入对象
     *
     * @param obj 要注入的对象
     */
    void injection(Object obj);
}