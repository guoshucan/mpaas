package ghost.framework.context.log.loader;

import ghost.framework.context.bean.factory.IBeanTargetHandle;
import ghost.framework.context.loader.ILoader;
import ghost.framework.context.log.IGetLog;
import org.apache.commons.logging.Log;

/**
 * package: ghost.framework.context.loader
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:加载接口，作为加载与卸载的基础接口
 * @Date: 2020/1/14:13:12
 * @param <O> 发起方对象
 * @param <T> 目标对象
 * @param <E> 处理事件对象
 */
public interface ILogLoader<O, T, E extends IBeanTargetHandle<O, T>> extends ILoader<O, T, E>, IGetLog {
    /**
     * 获取日志
     *
     * @return
     */
    @Override
    Log getLog();
}