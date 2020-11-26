package ghost.framework.core.module;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.module.ModuleArtifact;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.application.IApplication;
import ghost.framework.beans.application.event.ApplicationEventTopic;
import ghost.framework.beans.application.event.GenericApplicationEvent;
import ghost.framework.context.event.container.IEventFactoryContainer;
import ghost.framework.context.module.IModule;
import ghost.framework.context.module.IModuleContainer;
import ghost.framework.maven.FileArtifact;
import ghost.framework.util.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.aether.artifact.Artifact;

import java.io.File;
import java.util.*;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块容器类
 * @Date: 0:04 2020/1/13
 */
@Component
public class ModuleContainer extends AbstractMap<String, IModule> implements IModuleContainer {
    /**
     * 日志
     */
     private Log log = LogFactory.getLog(ModuleContainer.class);
    /**
     * 应用接口
     */
    private IApplication app;

    private Map<String, IModule> map = new HashMap<>();

    @Override
    public Set<Entry<String, IModule>> entrySet() {
        return map.entrySet();
    }

    /**
     * 添加模块
     * @param key 模块名称
     * @param value 模块接口
     * @return
     */
    @Override
    public IModule put(String key, IModule value) {
        Assert.notNullOrEmpty(key, "put is key null or empty error");
        Assert.notNull(value, "put is value null error");
        try {
            synchronized (map) {
                return map.put(key, value);
            }
        } finally {
            //发送加载插件事件
            //在此才发起加载插件事件处理加载插件避免插件类型构建注入等操作找不到当前模块对象问题
            value.publishEvent(new GenericApplicationEvent(value, ApplicationEventTopic.ModuleLaunchPlugin.name()));
        }
    }

    @Override
    public IModule remove(Object key) {
        Assert.notNullOrEmpty(key, "remove is key null or empty error");
        return super.remove(key);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return this.remove(key) != null;
    }

    /**
     * 获取全部模块事件监听容器接口列表
     *
     * @return
     */
    @Override
    public List<IEventFactoryContainer> getEventListenerContainerList() {
        List<IEventFactoryContainer> list = new ArrayList<>();
        synchronized (this.map) {
            for (Map.Entry<String, IModule> entry : this.map.entrySet()) {
                list.add(entry.getValue().getBean(IEventFactoryContainer.class));
            }
        }
        return list;
    }

    /**
     * 初始化模块容器类
     *
     * @param app 应用接口
     */
    public ModuleContainer(@Autowired IApplication app) {
        this.app = app;
        this.log.info("~" + this.getClass().getName());
    }

    /**
     * 使用类型获取所属模块
     *
     * @param target 类型目标
     * @return
     */
    @Override
    public IModule getApplicationHomeModule(Class<?> target) {
        Assert.notNull(target, "getApplicationHomeModule is target null error");
        final File file = new File(target.getProtectionDomain().getCodeSource().getLocation().getPath());
        synchronized (this.map) {
            //遍历模块版本比对
            for (IModule module : this.map.values()) {
                //比对模块版本
                if (module.getHome().getSource().equals(file)) {
                    //找到模块版本
                    return module;
                }
            }
        }
        return null;
    }

    @Override
    public IModule getModule(Artifact artifact) {
        Assert.notNull(artifact, "getModule is artifact null error");
        synchronized (this.map) {
            //遍历模块版本比对
            for (IModule module : this.map.values()) {
                //比对模块版本
                if (module.getArtifact().getGroupId().equals(artifact.getGroupId()) &&
                        module.getArtifact().getArtifactId().equals(artifact.getArtifactId()) &&
                        module.getArtifact().getVersion().equals(artifact.getVersion())) {
                    //找到模块版本
                    return module;
                }
            }
        }
        return null;
    }

    @Override
    public IModule getModule(FileArtifact artifact) {
        Assert.notNull(artifact, "getModule is artifact null error");
        synchronized (this.map) {
            //遍历模块版本比对
            for (IModule module : this.map.values()) {
                //比对模块版本
                if (module.getArtifact().getGroupId().equals(artifact.getGroupId()) &&
                        module.getArtifact().getArtifactId().equals(artifact.getArtifactId()) &&
                        module.getArtifact().getVersion().equals(artifact.getVersion())) {
                    //找到模块版本
                    return module;
                }
            }
        }
        return null;
    }

    /**
     * 获取模块
     *
     * @param artifact 模块版本信息注释
     * @return 返回获取的模块
     */
    @Override
    public IModule getModule(ModuleArtifact artifact) {
        Assert.notNull(artifact, "getModule is artifact null error");
        synchronized (this.map) {
            //遍历模块版本比对
            for (IModule module : this.map.values()) {
                //比对模块版本
                if (module.getArtifact().getGroupId().equals(artifact.groupId()) &&
                        module.getArtifact().getArtifactId().equals(artifact.artifactId()) &&
                        module.getArtifact().getVersion().equals(artifact.version())) {
                    //找到模块版本
                    return module;
                }
            }
        }
        return null;
    }

    /**
     * 获取模块
     *
     * @param name 模块名称
     * @return 返回获取的模块
     */
    @Override
    public IModule getModule(String name) {
        Assert.notNullOrEmpty(name, "getModule is name null or empty error");
        synchronized (this.map) {
            return this.map.get(name);
        }
    }
    /**
     * 删除模块
     *
     * @param name 模块名称
     * @return 返回删除的模块
     */
    @Override
    public IModule removeModule(String name) {
        Assert.notNullOrEmpty(name, "removeModule is name null or empty error");
        synchronized (this.map) {
            return this.map.remove(name);
        }
    }
    /**
     * 验证模块是否存在
     *
     * @param artifact 版本信息
     * @return
     */
    @Override
    public boolean containsModule(Artifact artifact) {
        Assert.notNull(artifact, "containsModule is artifact null error");
        synchronized (this.map) {
            for (IModule module : this.map.values()) {
                if (module.getArtifact().equals(artifact)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 验证模块是否存在
     *
     * @param name 模块名称
     * @return
     */
    @Override
    public boolean containsModule(String name) {
        Assert.notNullOrEmpty(name, "containsModule is name null or empty error");
        synchronized (this.map) {
            return this.map.containsKey(name);
        }
    }
    /**
     * 验证模块是否存在
     * @param groupId 模块组id
     * @param artifactId 模块组织id
     * @param version 模块版本
     * @return
     */
    @Override
    public boolean containsModule(String groupId, String artifactId, String version) {
        synchronized (this.map){
           return this.map.values().stream().anyMatch(a -> a.contains(groupId, artifactId, version));
        }
    }
    /**
     * 验证模块是否存在
     *
     * @param module 模块对象
     * @return
     */
    @Override
    public boolean containsModule(IModule module) {
        Assert.notNull(module, "containsModule is module null error");
        synchronized (this.map) {
            return this.containsValue(module);
        }
    }
    /**
     * 删除模块
     * @param artifact 模块信息
     */
    @Override
    public void removeModule(Artifact artifact) {

    }

    /**
     * 删除模块
     *
     * @param module 模块对象
     */
    @Override
    public void removeModule(IModule module) {
        Assert.notNull(module, "removeModule is module null error");
        synchronized (this.map) {
            this.map.remove(module.getName());
        }
    }
}