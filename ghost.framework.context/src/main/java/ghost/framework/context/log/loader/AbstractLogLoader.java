package ghost.framework.context.log.loader;

import ghost.framework.context.bean.factory.IBeanTargetHandle;
import ghost.framework.context.log.IGetLog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * package: ghost.framework.context.loader
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/1/14:13:13
 */
public abstract class AbstractLogLoader<O, T, E extends IBeanTargetHandle<O, T>> implements ILogLoader<O, T, E>, IGetLog {
    /**
     * 日志
     */
    private Log log = LogFactory.getLog(this.getClass());

    /**
     * 获取日志
     *
     * @return
     */
    @Override
    public Log getLog() {
        return log;
    }
}
