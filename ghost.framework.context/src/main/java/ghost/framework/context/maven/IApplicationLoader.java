package ghost.framework.context.maven;

import ghost.framework.context.bean.factory.IBeanTargetHandle;
import ghost.framework.context.loader.ILoader;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用加载处理接口
 * @Date: 16:26 2020/1/15
 */
public interface IApplicationLoader<O, T, E extends IBeanTargetHandle<O, T>> extends ILoader<O, T, E> {
}