package ghost.framework.data.jdbc.jpa.plugin.repository.support;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.invoke.Loader;
import ghost.framework.beans.annotation.invoke.Unloader;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.context.base.ICoreInterface;

/**
 * package: ghost.framework.data.jdbc.jpa.plugin.repository.support
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/7/8:23:52
 */
@Configuration
public class JpaRepositoryConfig {
    @Autowired
    private ICoreInterface coreInterface;
    @Loader
    public void loader(){
        this.coreInterface.addBean(JpaRepositoryFactory.class);
    }
    @Unloader
    public void unloader(){

    }
}