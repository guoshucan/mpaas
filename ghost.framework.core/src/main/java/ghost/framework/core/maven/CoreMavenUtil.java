package ghost.framework.core.maven;

import ghost.framework.context.assembly.BeansMavenUtil;
import ghost.framework.beans.maven.annotation.MavenDepository;
import ghost.framework.maven.MavenRepositoryServer;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:maven工具类
 * @Date: 12:38 2019/11/30
 */
public final class CoreMavenUtil {
    /**
     * 获取类型注释maven仓库列表
     *
     * @param c
     * @return
     */
    public static List<MavenRepositoryServer> getAnnotationMavenRepositoryList(Class<?> c) {
        MavenDepository[] depositorys = BeansMavenUtil.getDepositorys(c);
        List<MavenRepositoryServer> list = new ArrayList<>();
        if (depositorys != null) {
            for (MavenDepository depository : depositorys) {
                list.add(new MavenRepositoryServer(depository.id(), depository.type(), depository.username(), depository.password(), depository.url()));
            }
        }
        return list;
    }

    /**
     * 将字符maven仓库信息转换对象
     * 仓库文档参数格式maven-public,default,Username,Password,http://www.xxx.com:8081/repository/maven-public/
     *
     * @param repository
     * @return
     * @throws IllegalArgumentException
     */
    public static MavenRepositoryServer toMavenRepository(String repository) throws IllegalArgumentException {
        MavenRepositoryServer r = new MavenRepositoryServer();
        String[] strings = StringUtils.split(repository, ",");
        r.setId(StringUtils.isEmpty(strings[0]) ? null : strings[0]);
        if (strings.length == 2) {
            //没有类型账号密码长度
            r.setUrl(strings[1]);
        } else if (strings.length == 3) {
            //没有账号密码长度
            r.setType(StringUtils.isEmpty(strings[1]) ? null : strings[1]);
            r.setUrl(strings[2]);
        } else {
            r.setUsername(StringUtils.isEmpty(strings[2]) ? null : strings[2]);
            r.setPassword(StringUtils.isEmpty(strings[3]) ? null : strings[3]);
            r.setUrl(strings[4]);
        }
        if (StringUtils.isEmpty(r.getUrl())) {
            throw new IllegalArgumentException("MavenRepository url is null");
        }
        return r;
    }

    /**
     * 将字符maven仓库信息转换对象
     * 仓库文档参数格式maven-public,default,Username,Password,http://www.xxx.com:8081/repository/maven-public/|maven-public,default,Username,Password,http://www.xxx.com:8081/repository/maven-public/
     *
     * @param repositorys
     * @return
     * @throws IllegalArgumentException
     */
    public static List<MavenRepositoryServer> toMavenRepositoryList(String repositorys) throws IllegalArgumentException {
        List<MavenRepositoryServer> list = new ArrayList<>();
        String[] strings = StringUtils.split(repositorys, "|");
        for (String s : strings) {
            list.add(toMavenRepository(s));
        }
        return list;
    }
}
