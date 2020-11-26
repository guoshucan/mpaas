package ghost.framework.context.maven;

import ghost.framework.maven.MavenRepositoryServer;

import java.util.Collection;
import java.util.List;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 11:28 2019/12/14
 */
public interface IMavenRepositoryContainer extends List<MavenRepositoryServer> {
    boolean addNotContains(MavenRepositoryServer repository);
    void addNotContainsAll(List<MavenRepositoryServer> repositoryServerList);
    void addNotContainsAll(Collection<? extends MavenRepositoryServer> c);
}