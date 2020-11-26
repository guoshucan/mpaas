package ghost.framework.application.core.loader.maven;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.event.maven.IMavenLoaderEventTargetHandle;
import ghost.framework.context.maven.IMavenDepositoryLoader;
import ghost.framework.context.maven.IMavenLoader;
import ghost.framework.context.maven.IMavenModuleLoader;
import ghost.framework.context.module.IModuleClassLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用maven加载类
 * @Date: 8:09 2019/12/17
 * @param <O> 发起发类型
 * @param <T> 事件目标类型
 * @param <M> 模块类加载器类型
 * @param <E> maven加载事件目标处理类型
 */
@Component
public class MavenLoader
        <
                O extends ICoreInterface,
                T extends Object,
                M extends IModuleClassLoader,
                E extends IMavenLoaderEventTargetHandle<O, T, M>>
//        extends AbstractApplicationMavenLoader<O, T, E>
        implements IMavenLoader<O, T, M, E> {
    /**
     * 初始化应用maven加载类
     *
     * @param app 应用接口
     */
    public MavenLoader(@Autowired IApplication app) {
        this.app = app;
    }

    private IApplication app;
    private Log log = LogFactory.getLog(MavenLoader.class);
    /**
     * 加载maven包
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        this.log.info("loader:" + event.toString());
        //maven加载顺序按照下面的顺序
        //1、注释maven仓库注释
        event.getOwner().getBean(IMavenDepositoryLoader.class).loader(event);
        //2、注释maven应用依赖包
//        event.getOwner().getBean(IMavenApplicationDependencyLoader.class).loader(event);
        //3、注释maven模块依赖包
        event.getOwner().getBean(IMavenModuleLoader.class).loader(event);
    }
}