package ghost.framework.application.core.loader.maven;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.maven.annotation.MavenDepository;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.event.maven.IMavenLoaderEventTargetHandle;
import ghost.framework.context.maven.IMavenDepositoryLoader;
import ghost.framework.context.maven.IMavenRepositoryContainer;
import ghost.framework.context.module.IModuleClassLoader;
import ghost.framework.core.maven.annotation.reader.IAnnotationMavenDepositoryReader;
import ghost.framework.maven.MavenRepositoryServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用maven仓库加载器
 * @Date: 9:59 2019/12/8
 * @param <O> 发起方类型
 * @param <T> 目标对象
 * @param <M> 模块类加载器接口类型
 * @param <E> maven加载事件目标处理类型接口
 */
@Component
public class MavenDepositoryLoader
        <
                O extends ICoreInterface,
                T,
                M extends IModuleClassLoader,
                E extends IMavenLoaderEventTargetHandle<O, T, M>
                >
//        extends AbstractApplicationMavenLoader<O, T, E>
        implements IMavenDepositoryLoader<O, T, M, E> {
    /**
     * 初始化应用maven仓库加载器
     *
     * @param app 设置应用接口
     */
    public MavenDepositoryLoader(@Autowired IApplication app) {
        this.app = app;
    }

    private IApplication app;
    private Log log = LogFactory.getLog(MavenDepositoryLoader.class);

    /**
     * 加载maven仓库
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        this.log.info("loader:" + event.toString());
        //maven仓库容器
        IMavenRepositoryContainer mavenRepositories = this.app.getBean(IMavenRepositoryContainer.class);
        //判断加载类型
        if (event.getTarget() instanceof Class) {
            //获取类型加载类
            Class<?> c = (Class<?>) event.getTarget();
            //遍历注释maven仓库
            for (MavenDepository depository : this.app.getBean(IAnnotationMavenDepositoryReader.class).getList(c)) {
                this.log.info(depository.toString());
                MavenRepositoryServer repository = new MavenRepositoryServer(depository.id(), depository.type(), depository.username(), depository.password(), depository.url());
                if (mavenRepositories.addNotContains(repository)) {
                    this.log.info("add MavenRepositoryServer:" + repository.toString());
                }
            }
        }
    }

    /**
     * 卸载maven仓库
     *
     * @param event 事件对象
     */
    @Override
    public void unloader(E event) {
        this.log.info("unloader:" + event.toString());
        //maven仓库容器
        IMavenRepositoryContainer mavenRepositories = this.app.getBean(IMavenRepositoryContainer.class);
        //判断加载类型
        if (event.getTarget() instanceof Class) {
            //获取类型加载类
            Class<?> c = (Class<?>) event.getTarget();
            //遍历注释maven仓库
            for (MavenDepository depository : this.app.getBean(IAnnotationMavenDepositoryReader.class).getList(c)) {
                MavenRepositoryServer repository = new MavenRepositoryServer(depository.id(), depository.type(), depository.username(), depository.password(), depository.url());
                mavenRepositories.remove(repository);
                this.log.info("remove MavenRepositoryServer:" + repository.toString());
            }
        }
    }
}