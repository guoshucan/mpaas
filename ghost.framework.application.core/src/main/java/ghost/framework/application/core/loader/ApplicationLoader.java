package ghost.framework.application.core.loader;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.stereotype.IClassApplicationBootAnnotationBeanFactory;
import ghost.framework.context.event.maven.IMavenLoaderEventTargetHandle;
import ghost.framework.context.maven.IApplicationLoader;
import ghost.framework.context.maven.IMavenLoader;
import ghost.framework.context.module.IModuleClassLoader;
import ghost.framework.context.bean.factory.ClassAnnotationBeanTargetHandle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用加载处理
 * @Date: 16:24 2020/1/15
 */
@Component
public final class ApplicationLoader<
        O extends ICoreInterface,
        T extends Object,
        M extends IModuleClassLoader,
        E extends IMavenLoaderEventTargetHandle<O, T, M>>
        implements IApplicationLoader<O, T, E> {
    /**
     * 初始化应用加载处理
     *
     * @param app 应用接口
     */
    public ApplicationLoader(@Autowired IApplication app) {
        this.app = app;
    }

    private IApplication app;
    private Log log = LogFactory.getLog(ApplicationLoader.class);

    /**
     * 加载运行类
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        this.log.info("loader:" + event.toString());
        //加载maven操作
        event.getOwner().getBean(IMavenLoader.class).loader(event);
        //maven加载完成
        //处理加载Boot注释
        event.getOwner().getBean(IClassApplicationBootAnnotationBeanFactory.class).loader(new ClassAnnotationBeanTargetHandle(this.app, this.app.getRootClass()));
    }

    /**
     * @param event 事件对象
     */
    @Override
    public void unloader(E event) {
        this.log.info("unloader:" + event.toString());
    }
}