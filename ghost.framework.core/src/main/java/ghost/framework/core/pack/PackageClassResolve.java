package ghost.framework.core.pack;

import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.pack.IPackageClassResolve;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * package: ghost.framework.packageclassresolve.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:包类型解析类
 * @Date: 2020/8/1:18:07
 */
@Component
public class PackageClassResolve implements IPackageClassResolve {
    private final Log log = LogFactory.getLog(PackageClassResolve.class);
    @Override
    public Log getLog() {
        return log;
    }
}