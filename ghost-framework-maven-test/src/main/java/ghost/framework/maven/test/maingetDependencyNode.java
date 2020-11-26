package ghost.framework.maven.test;

import ghost.framework.maven.Booter;
import ghost.framework.maven.Params;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.resolution.DependencyResolutionException;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 21:41 2019-05-30
 */
final class maingetDependencyNode {
    public static void main(String... strings) throws Exception {
//        DefaultArtifact params = new DefaultArtifact("ghost.framework.boot", "spring-boot", null, "2.1.4.RELEASE");
//        //params.setVersion("[0,)");
//        DependencyNode node = Booter.getDependencyNode(params);
//        for (DependencyNode v : node.getChildren()) {
//            System.out.println(v.toString());
//        }
        //        DefaultArtifact params = new DefaultArtifact("ghost.framework.boot", "spring-boot", null, "2.1.4.RELEASE");
        DefaultArtifact params = new DefaultArtifact("com.zaxxer", "HikariCP", "", "3.2.0");
//        DefaultArtifact params = new DefaultArtifact("commons-collections", "commons-collections", null, "3.2.2");
        //params.setVersion("[0,)");
        Params p = new Params(params);
        Booter.download(p, Booter.newRepositories());
//        DependencyNode node = Booter.getDependencyNode(params);
//        for (DependencyNode v : node.getChildren()) {
//            System.out.println(v.toString());
//        }
    }
}
