package ghost.framework.core.assembly;

import ghost.framework.beans.maven.IGetArtifacts;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.utils.AssemblyUtil;
import ghost.framework.context.application.ApplicationConstant;
import ghost.framework.maven.ArtifactManifest;
import ghost.framework.maven.FileArtifact;
import ghost.framework.maven.IArtifactManifest;
import ghost.framework.maven.MavenArtifactException;
import ghost.framework.util.ReflectUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.aether.artifact.Artifact;
import sun.misc.Resource;
import sun.misc.URLClassPath;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLStreamHandlerFactory;
import java.security.AccessControlContext;
import java.util.*;
import java.util.jar.Manifest;
/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:url包加载列表
 * @Date: 17:00 2019-07-14
 */
public class URLPaths extends URLClassPath implements IGetArtifacts {
    private Log log = LogFactory.getLog(URLPaths.class);

    /**
     * @return
     */
    @Override
    public List<FileArtifact> getArtifacts() {
        return null;
    }

    /**
     * 初始化url包加载列表
     *
     * @param urls                    初始化包列表
     * @param urlStreamHandlerFactory
     * @param accessControlContext
     */
    public URLPaths(IApplication app, URL[] urls, URLStreamHandlerFactory urlStreamHandlerFactory, AccessControlContext accessControlContext) {
        super(urls, urlStreamHandlerFactory, accessControlContext);
        this.app = app;
    }

    private final IApplication app;

    /**
     * 初始化url包加载列表
     *
     * @param urls 初始化包列表
     */
    public URLPaths(IApplication app, URL[] urls) {
        super(urls);
        this.app = app;
    }

    /**
     * 初始化url包加载列表
     *
     * @param accessControlContext
     */
    public URLPaths(IApplication app, AccessControlContext accessControlContext) {
        super(new URL[]{}, accessControlContext);
        this.app = app;
    }

    /**
     * 初始化url包加载列表
     *
     * @param urls                 初始化包列表
     * @param accessControlContext
     */
    public URLPaths(IApplication app, URL[] urls, AccessControlContext accessControlContext) {
        super(urls, accessControlContext);
        this.app = app;
    }

    public static InputStream getResourceAsStream(InputStream in, URLPaths ucp, String name) {
        //判断从基础加载器是否获取到资源流
        if (in == null) {
            //没有从基础加载器获取到资源流，从加载包获取资源流
            try {
                in = ucp.getResource(name, false).getInputStream();
            } catch (IOException e) {
                //不处理错误
            }
        }
        return in;
    }

    public static URL getResource(URL url, URLPaths ucp, String name) {
        //基础获取不到该资源utl
        if (url == null) {
            //从加载包列表中获取资源url
            return ucp.findResource(name);
        }
        return url;
    }

    public static Enumeration<URL> findResources(Enumeration<URL> tmp, URLPaths ucp, String name) {
        //创建集合
        Vector<URL> v = new Vector<>();
        //装载父级资源url列表
        while (tmp.hasMoreElements()) {
            v.add(tmp.nextElement());
        }
        //获取加载包资源列表
        tmp = ucp.findResources(name, false);
        while (tmp.hasMoreElements()) {
            v.add(tmp.nextElement());
        }
        return v.elements();
    }

    public static URL findResource(URL url, URLPaths ucp, String name) {
        //判断从基础类加载器搜索资源是否有效
        if (url == null) {
            //资源无效，在加载包搜索资源
            url = ucp.findResource(name, false);
        }
        return url;
    }

    public static Enumeration<URL> getResources(Enumeration<URL> tmp, URLPaths ucp, String name) {
        //创建集合
        Vector<URL> v = new Vector<>();
        //装载父级资源url列表
        while (tmp.hasMoreElements()) {
            v.add(tmp.nextElement());
        }
        //获取加载包资源列表
        Enumeration<Resource> resources = ucp.getResources(name, false);
        while (resources.hasMoreElements()) {
            v.add(resources.nextElement().getURL());
        }
        return v.elements();
    }

    /**
     * 重写添加类
     *
     * @param url
     */
    @Override
    public synchronized void addURL(URL url) {
        //遍历url列表
//        for (URL u : this.getURLs()) {
//            //判断版本是否重复
//            try {
//                if (AssemblyUtil.getManifest(url).equals(AssemblyUtil.getManifest(u))) {
//                    //版本重复退出添加包
//                    return;
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        super.addURL(url);
    }

    /**
     * 删除包
     *
     * @param url
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws IOException
     */
    public void remove(URL url) throws IllegalArgumentException, IllegalAccessException, IOException {
        //获取url包列表
        ArrayList<URL> path = ReflectUtil.findField(this, "path");
        Stack<URL> urls = ReflectUtil.findField(this, "urls");
        synchronized (urls) {
            synchronized (path) {
                //删除包列表
                urls.remove(url);
                path.remove(url);
            }
        }
        //获取已经加载的包列表
        ArrayList<Object> loaders = ReflectUtil.findField(this, "loaders");
        synchronized (loaders) {
            for (Object o : loaders) {
                //获取已经加载的包url
                URL base = ReflectUtil.findField(o, "base");
                //判断是否为要删除的包url
                if (base.equals(url)) {
                    //找到包url
                    loaders.remove(o);
                    ((Closeable) o).close();
                    break;
                }
            }
        }
    }

    /**
     * 不做安全检查获取资源url
     *
     * @param name 资源名称
     * @return
     */
    public URL findResource(String name) {
        return this.findResource(name, false);
    }

    /**
     * 验证包是否存在
     *
     * @param url
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public boolean contains(URL url) throws IllegalArgumentException, IllegalAccessException {
        Stack<URL> urls = ReflectUtil.findField(this, "urls");
        synchronized (urls) {
            for (URL u : urls) {
                if (u.equals(url)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 验证包是否已经存在
     *
     * @param artifact 包数据
     * @return
     * @throws Exception
     */
    public boolean contains(Artifact artifact) throws MavenArtifactException {
        //获取包列表
        Stack<URL> urls = ReflectUtil.findField(this, "urls");
        synchronized (urls) {
            try {
                //遍历包列表
                for (URL u : urls) {
                    //获取包版本信息
                    Manifest manifest = AssemblyUtil.getManifest(u);
                    if (manifest == null) {
                        throw new IOException(artifact.toString());
                    }
                    IArtifactManifest artifactManifest = new ArtifactManifest(manifest);
                    //比对包版本信息
                    if (artifactManifest.equals(artifact)) {
                        if (artifact instanceof FileArtifact) {
                            ((FileArtifact) artifact).setFile(new File(u.getFile()));
                        }
                        return true;
                    }
                }
            } catch (URISyntaxException | IOException | XmlPullParserException e) {
                throw new MavenArtifactException(e.getMessage(), e);
            }
        }
        return false;
    }

    /**
     * 判断包文件是否存在
     *
     * @param file
     * @return
     * @throws MalformedURLException
     */
    public boolean contains(File file) throws MalformedURLException {
        URL url = file.toURI().toURL();
        //获取包列表
        Stack<URL> urls = ReflectUtil.findField(this, "urls");
        synchronized (urls) {
            //遍历包列表
            for (URL u : urls) {
                if (url.equals(u)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取包的URL
     * {@link ghost.framework.context.application.IApplicationEnvironment#getString(String)}
     *
     * @param artifact 包信息
     * @return 返回指定包信息的包URL
     */
    public URL getURL(Artifact artifact) {
        //获取应用maven本地仓库路径
        URL url = this.app.getBean(ApplicationConstant.Maven.LOCAL_REPOSITORY_URL);
        final String rootPath = url.toString();
        String path;
        String[] strings;
        //获取包列表
        Stack<URL> urls = ReflectUtil.findField(this, "urls");
        synchronized (urls) {
            //遍历包列表
            for (URL u : urls) {
                path = u.toString();
                if (path.startsWith(rootPath)) {
                    path = path.substring(rootPath.length());
                }
                strings = path.split("/");
                String groupId = null;
                String artifactId = strings[strings.length - 3];
                String version = strings[strings.length - 2];
                for (int i = 0; i < strings.length - 3; i++) {
                    if (groupId == null) {
                        groupId = strings[i];
                    } else {
                        groupId += "." + strings[i];
                    }
                }
                System.out.println("Manifest:" + groupId + ":" + artifactId + "::" + version + "::Url:" + u.toString());
                //比对包版本信息
                if (Objects.equals(groupId, artifact.getGroupId()) &&
                        Objects.equals(artifactId, artifact.getArtifactId()) &&
                        Objects.equals(version, artifact.getVersion())) {
                    return u;
                }
            }
        }
        return null;
    }
}