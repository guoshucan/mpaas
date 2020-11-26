package ghost.framework.context.application;

import ghost.framework.context.bean.IBean;
import ghost.framework.context.assembly.IGetClassLoader;
import ghost.framework.context.IGetDev;
import ghost.framework.context.IGetInitialize;
import ghost.framework.context.IGetName;
import ghost.framework.context.IGetRootClass;
import ghost.framework.context.base.*;
import ghost.framework.context.converter.IGetConverterContainer;
import ghost.framework.context.log.IGetLog;
import ghost.framework.context.maven.IGetMavenLocalRepositoryFile;
import ghost.framework.context.maven.IGetMavenRepositoryContainer;
import ghost.framework.context.module.IContainsModule;
import ghost.framework.context.module.IGetModule;
import ghost.framework.context.module.IRemoveModule;
import ghost.framework.context.plugin.IGetPluginContainer;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用接口
 * @Date: 1:15 2019-05-28
 */
public interface IApplication extends
        AutoCloseable, IBean, IBase, IObjectInjection,
        ICoreInterface, IGetModule, IContainsModule, IRemoveModule,
        IInstance, IGetInitialize, IGetRootClass, IGetDev, IGetName, IGetHome,
        IGetTempDirectory, IGetLog, IGetMavenLocalRepositoryFile, IGetClassLoader, IGetPluginContainer,
        IGetMavenRepositoryContainer, IGetConverterContainer {
}