package ghost.framework.context.loader;

import ghost.framework.context.bean.factory.IBeanTargetHandle;
import ghost.framework.context.log.IGetLog;

/**
 * package: ghost.framework.context.loader
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/1/14:13:13
 */
public abstract class AbstractLoader<O, T, E extends IBeanTargetHandle<O, T>> implements ILoader<O, T, E>, IGetLog {
}
