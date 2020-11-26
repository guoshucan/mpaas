package ghost.framework.maven.test;

import ghost.framework.maven.Booter;
import ghost.framework.maven.Params;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.version.Version;

import java.util.List;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 16:21 2019-05-27
 */
final class mainGetAllVersions {
    public static void main(String... strings) throws VersionRangeResolutionException {
        Params params = new Params();
        params.setArtifactId("commons-collections");
        params.setGroupId("commons-collections");
        //params.setVersion("[0,)");
        List<Version> list = Booter.getAllVersions(params);
        for (Version v : list) {
            System.out.println(v.toString());
        }
    }
}
