package ghost.framework.core.pack;

import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.pack.IClassResolve;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * package: ghost.framework.core.pack
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型解析类型
 * @Date: 2020/8/2:12:34
 */
@Component
public class ClassResolve implements IClassResolve {
    private final Log log = LogFactory.getLog(ClassResolve.class);

    @Override
    public Log getLog() {
        return log;
    }
}
