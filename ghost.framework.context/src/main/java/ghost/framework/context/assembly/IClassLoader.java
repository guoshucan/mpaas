package ghost.framework.context.assembly;

import ghost.framework.beans.maven.IGetArtifacts;
import ghost.framework.maven.FileArtifact;
import ghost.framework.maven.MavenArtifactException;
import ghost.framework.util.FileUtil;
import org.eclipse.aether.artifact.Artifact;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类加载器接口
 * @Date: 2:13 2019/12/18
 */
public interface IClassLoader extends IGetArtifacts {
    /**
     * 判断包是否存在
     *
     * @param artifact
     * @return
     * @throws Exception
     */
    boolean contains(Artifact artifact) throws MavenArtifactException;

    /**
     * 判断包是否存在
     *
     * @param file
     * @return
     * @throws MalformedURLException
     */
    boolean contains(File file) throws MalformedURLException;

    boolean contains(String packName);

    /**
     * 添加包
     *
     * @param file
     * @throws MalformedURLException
     */
    void add(File file) throws MalformedURLException;

    /**
     * 添加包列表
     *
     * @param urls
     */
    default void addURL(URL[] urls) {
        for (URL url : urls) {
            this.addURL(url);
        }
    }

    /**
     * 添加包列表
     *
     * @param files
     * @throws MalformedURLException
     */
    default void add(List<File> files) throws MalformedURLException {
        for (File file : files) {
            this.addURL(FileUtil.toURL(file));
        }
    }

    /**
     * 添加包
     *
     * @param url
     */
    void addURL(URL url);

    /**
     * 判断包是否存在
     *
     * @param url
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    boolean contains(URL url) throws IllegalArgumentException, IllegalAccessException;

    /**
     * 删除包
     *
     * @param url
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws IOException
     */
    void removeURL(URL url) throws IllegalArgumentException, IllegalAccessException, IOException;

    /**
     * 过滤重复
     *
     * @param fileArtifactList
     * @throws MalformedURLException
     */
    void filterRepeat(List<FileArtifact> fileArtifactList) throws MalformedURLException;

    /**
     * 添加包列表
     *
     * @param artifactList
     * @throws MalformedURLException
     */
    default void addArtifactList(List<FileArtifact> artifactList) throws MalformedURLException {
        for (FileArtifact artifact : artifactList) {
            this.addURL(artifact.getFile().toURI().toURL());
        }
    }

    /**
     * 获取名称类型
     *
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    Class<?> loadClass(String name) throws ClassNotFoundException;

    /**
     * 删除包
     *
     * @param file
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws IOException
     */
    default void remove(File file) throws IllegalArgumentException, IllegalAccessException, IOException {
        this.removeURL(file.toURL());
    }
    /**
     * 删除包
     * @param artifact
     */
    default void removeArtifact(FileArtifact artifact) {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param artifactList
     */
    default void removeArtifactList(List<FileArtifact> artifactList) {
        artifactList.forEach(this::removeArtifact);
    }

    /**
     * 获取包的URL
     * @param artifact 包信息
     * @return 返回指定包信息的包URL
     */
    URL getURL(Artifact artifact);
}