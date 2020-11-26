package ghost.framework.core.application.reader;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.application.IApplication;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 19:38 2020/1/12
 */
public abstract class AbstractApplicationReader implements IApplicationReader {
    @Autowired
    private IApplication app;

    @Override
    public IApplication getApp() {
        return app;
    }
}
