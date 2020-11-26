package ghost.framework.module.assembly;
import ghost.framework.context.module.IModule;
import ghost.framework.maven.AssemblyUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块url引用类
 * @Date: 14:48 2019-06-23
 */
public final class ModuleUrlReference {
    public ModuleUrlReference(ModuleClassLoader loader) {
        this.loader = loader;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + this.contents.hashCode() + this.loader.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ModuleUrlReference) {
            ModuleUrlReference r = (ModuleUrlReference) obj;
            if (this.contents.equals(r.contents)) {
                return true;
            }
        }
        return super.equals(obj);
    }

    private ModuleClassLoader loader;
    /**
     * 包依赖的模块列表
     * CodeSource
     */
    private Map<URL, List<IModule>> contents = new HashMap<>();

    public Map<URL, List<IModule>> getContents() {
        return contents;
    }

    public void add(IModule content, URL url) {
        synchronized (this.contents) {
            List<IModule> l = new ArrayList<>();
            if (content != null) {
                l.add(content);
            }
            this.contents.put(url, l);
        }
    }

    public void remove(URL url) {
        synchronized (this.contents) {
            this.contents.remove(url);
        }
    }

    public void add(URL url) {
        synchronized (this.contents) {
            this.contents.put(url, new ArrayList<>());
        }
    }

    /**
     * 删除包引用的模块
     *
     * @param url     地址包
     * @param content 模块内容
     */
    public void remove(URL url, IModule content) {
        synchronized (this.contents) {
            if (content == null) {

            } else {
                this.contents.get(url).remove(content);
            }
        }
    }

    /**
     * 获取资源url
     *
     * @param name 资源名称
     * @return
     */
    public URL getResource(String name) {
        synchronized (this.contents) {
            for (URL url : this.contents.keySet()) {
                URL u = AssemblyUtil.getResourcePath(url, name);
                if (u != null) return u;
            }
        }
        return null;
    }
}
