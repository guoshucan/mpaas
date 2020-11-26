package ghost.framework.core.maven;

import ghost.framework.beans.annotation.constructor.Constructor;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.maven.IMavenRepositoryContainer;
import ghost.framework.maven.MavenRepositoryServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:maven仓库容器
 * @Date: 12:05 2019/11/30
 */
@Component
public class MavenRepositoryContainer extends ArrayList<MavenRepositoryServer> implements IMavenRepositoryContainer {
    /**
     * 日志
     */
     private Log log = LogFactory.getLog(this.getClass());
    /**
     * 初始化maven仓库容器
     */
    @Constructor
    public MavenRepositoryContainer() {
        super(5);
        this.log.info("~" + this.getClass().getName());
    }
    /**
     * 初始化maven仓库容器
     * @param repositoryList
     */
    public MavenRepositoryContainer(List<MavenRepositoryServer> repositoryList) {
        super(5);
        this.addNotContainsAll(repositoryList);
        this.log.info("~" + this.getClass().getName() + "(repositoryList:" + repositoryList.size() + ")");
    }
    /**
     * 添加不重复仓库
     *
     * @param repository 仓库列表
     * @return
     */
    @Override
    public boolean addNotContains(MavenRepositoryServer repository) {
        if (!this.contains(repository)) {
            return this.add(repository);
        }
        return false;
    }

    @Override
    public void addNotContainsAll(List<MavenRepositoryServer> repositoryServerList) {
        for (MavenRepositoryServer repository : repositoryServerList) {
            if (!this.contains(repository)) {
                this.add(repository);
            }
        }
    }

    /**
     * 添加不重复仓库
     *
     * @param c 仓库列表
     * @return
     */
    @Override
    public void addNotContainsAll(Collection<? extends MavenRepositoryServer> c) {
        for (MavenRepositoryServer repository : c) {
            if (!this.contains(repository)) {
                this.add(repository);
            }
        }
    }
}