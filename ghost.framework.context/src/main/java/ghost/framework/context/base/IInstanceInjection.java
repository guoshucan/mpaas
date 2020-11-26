package ghost.framework.context.base;

import ghost.framework.beans.annotation.constraints.NotNull;

/**
 * package: ghost.framework.context.base
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:构建注入接口
 * @Date: 2020/5/18:23:52
 */
public interface IInstanceInjection {
    /**
     * 构建类型后注入
     * @param c 构建类型
     * @return 返回构建后对象
     */
    @NotNull
    Object newInstanceInjection(@NotNull Class<?> c);
}
