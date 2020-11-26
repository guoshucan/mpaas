package ghost.framework.core.environment;
import ghost.framework.context.environment.Environment;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.core.event.environment.EnvironmentEventTargetHandle;
import ghost.framework.core.event.environment.factory.IEnvironmentEventListenerFactoryContainer;
import java.util.Date;
import java.util.Properties;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 15:04 2019/12/19
 */
public abstract class EventEnvironment extends Environment {
    /**
     * 应用模块env事件监听容器
     */
    protected IEnvironmentEventListenerFactoryContainer eventListenerFactoryContainer;
    /**
     * 获取env事件监听容器
     *
     * @return
     */
    protected abstract IEnvironmentEventListenerFactoryContainer getEventListenerFactoryContainer();

    /**
     * 获取拥有者
     *
     * @return
     */
    protected abstract ICoreInterface getTarget();

    /**
     * 初始化模块nev
     */
    public EventEnvironment() {
        super();
    }

    /**
     * 初始化模块nev
     *
     * @param env 合并env
     */
    public EventEnvironment(Environment env) {
        super();
        this.merge(env);
    }

    @Override
    public void clear() {
        super.clear();
        if (this.getEventListenerFactoryContainer() != null) {
            this.eventListenerFactoryContainer.envClear(new EnvironmentEventTargetHandle(this.getTarget(), this));
        }
    }

    @Override
    public void remove(String key) {
        super.remove(key);
        if (this.getEventListenerFactoryContainer() != null) {
            this.eventListenerFactoryContainer.envRemove(new EnvironmentEventTargetHandle(this.getTarget(), this), key);
        }
    }

    @Override
    public void setBoolean(String key, boolean v) {
        if (this.getEventListenerFactoryContainer() != null) {
            this.eventListenerFactoryContainer.envChangeBefore(new EnvironmentEventTargetHandle(this.getTarget(), this), key, v);
        }
        super.setBoolean(key, v);
        if (this.getEventListenerFactoryContainer() != null) {
            this.eventListenerFactoryContainer.envChangeAfter(new EnvironmentEventTargetHandle(this.getTarget(), this), key, v);
        }
    }

    @Override
    public void setByte(String key, byte v) {
        if (this.getEventListenerFactoryContainer() != null) {
            this.eventListenerFactoryContainer.envChangeBefore(new EnvironmentEventTargetHandle(this.getTarget(), this), key, v);
        }
        super.setByte(key, v);
        if (this.getEventListenerFactoryContainer() != null) {
            this.eventListenerFactoryContainer.envChangeAfter(new EnvironmentEventTargetHandle(this.getTarget(), this), key, v);
        }
    }

    @Override
    public void setDate(String key, Date v) {
        if (this.getEventListenerFactoryContainer() != null) {
            this.eventListenerFactoryContainer.envChangeBefore(new EnvironmentEventTargetHandle(this.getTarget(), this), key, v);
        }
        super.setDate(key, v);
        if (this.getEventListenerFactoryContainer() != null) {
            this.eventListenerFactoryContainer.envChangeAfter(new EnvironmentEventTargetHandle(this.getTarget(), this), key, v);
        }
    }

    @Override
    public void setDouble(String key, double v) {
        if (this.getEventListenerFactoryContainer() != null) {
            this.eventListenerFactoryContainer.envChangeBefore(new EnvironmentEventTargetHandle(this.getTarget(), this), key, v);
        }
        super.setDouble(key, v);
        if (this.getEventListenerFactoryContainer() != null) {
            this.eventListenerFactoryContainer.envChangeAfter(new EnvironmentEventTargetHandle(this.getTarget(), this), key, v);
        }
    }

    @Override
    public void setFloat(String key, float v) {
        if (this.getEventListenerFactoryContainer() != null) {
            this.eventListenerFactoryContainer.envChangeBefore(new EnvironmentEventTargetHandle(this.getTarget(), this), key, v);
        }
        super.setFloat(key, v);
        if (this.getEventListenerFactoryContainer() != null) {
            this.eventListenerFactoryContainer.envChangeAfter(new EnvironmentEventTargetHandle(this.getTarget(), this), key, v);
        }
    }

    @Override
    public void setInt(String key, int v) {
        if (this.getEventListenerFactoryContainer() != null) {
            this.eventListenerFactoryContainer.envChangeBefore(new EnvironmentEventTargetHandle(this.getTarget(), this), key, v);
        }
        super.setInt(key, v);
        if (this.getEventListenerFactoryContainer() != null) {
            this.eventListenerFactoryContainer.envChangeAfter(new EnvironmentEventTargetHandle(this.getTarget(), this), key, v);
        }
    }

    @Override
    public void setLong(String key, long v) {
        if (this.getEventListenerFactoryContainer() != null) {
            this.eventListenerFactoryContainer.envChangeBefore(new EnvironmentEventTargetHandle(this.getTarget(), this), key, v);
        }
        super.setLong(key, v);
        if (this.getEventListenerFactoryContainer() != null) {
            this.eventListenerFactoryContainer.envChangeAfter(new EnvironmentEventTargetHandle(this.getTarget(), this), key, v);
        }
    }

    @Override
    public void setShort(String key, short v) {
        if (this.getEventListenerFactoryContainer() != null) {
            this.eventListenerFactoryContainer.envChangeBefore(new EnvironmentEventTargetHandle(this.getTarget(), this), key, v);
        }
        super.setShort(key, v);
        if (this.getEventListenerFactoryContainer() != null) {
            this.eventListenerFactoryContainer.envChangeAfter(new EnvironmentEventTargetHandle(this.getTarget(), this), key, v);
        }
    }

    @Override
    public void setString(String key, String v) {
        if (this.getEventListenerFactoryContainer() != null) {
            this.eventListenerFactoryContainer.envChangeBefore(new EnvironmentEventTargetHandle(this.getTarget(), this), key, v);
        }
        super.setString(key, v);
        if (this.getEventListenerFactoryContainer() != null) {
            this.eventListenerFactoryContainer.envChangeAfter(new EnvironmentEventTargetHandle(this.getTarget(), this), key, v);
        }
    }

    @Override
    public void merge(Environment env) {
        if (this.getEventListenerFactoryContainer() != null) {
            this.eventListenerFactoryContainer.envMergeBefore(new EnvironmentEventTargetHandle(this.getTarget(), this), env);
        }
        super.merge(env);
        if (this.getEventListenerFactoryContainer() != null) {
            this.eventListenerFactoryContainer.envMergeBefore(new EnvironmentEventTargetHandle(this.getTarget(), this), env);
        }
    }

    @Override
    public void merge(Properties properties) {
        if (this.getEventListenerFactoryContainer() != null) {
            this.eventListenerFactoryContainer.envMergeBefore(new EnvironmentEventTargetHandle(this.getTarget(), this), properties);
        }
        super.merge(properties);
        if (this.getEventListenerFactoryContainer() != null) {
            this.eventListenerFactoryContainer.envMergeBefore(new EnvironmentEventTargetHandle(this.getTarget(), this), properties);
        }
    }
}