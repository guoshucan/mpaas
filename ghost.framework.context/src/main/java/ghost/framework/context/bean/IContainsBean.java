package ghost.framework.context.bean;

import ghost.framework.beans.annotation.constraints.NotNull;

/**
 * package: ghost.framework.context.bean
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/9:18:08
 */
public interface IContainsBean {
    /**
     * 验证绑定是否存在
     *
     * @param c 绑定类型
     * @return
     */
    boolean containsBean(@NotNull Class<?> c);

    /**
     * 验证绑定是否存在
     *
     * @param name 绑定id
     * @return
     */
    boolean containsBean(@NotNull String name);

    /**
     * 验证绑定是否存在
     *
     * @param obj
     * @return
     */
    boolean containsBean(@NotNull Object obj);
}
