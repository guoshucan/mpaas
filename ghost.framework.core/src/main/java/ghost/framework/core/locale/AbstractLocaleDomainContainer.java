package ghost.framework.core.locale;

import ghost.framework.beans.annotation.module.ModulePackage;
import ghost.framework.context.collections.generic.AbstractSyncSmartList;
import ghost.framework.context.locale.ILocaleDomain;
import ghost.framework.context.locale.ILocaleDomainContainer;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;

/**
 * package: ghost.framework.core.locale
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * 作为下面包注释的国际化域容器
 * {@link PluginPackage}
 * {@link ModulePackage}
 * @Date: 2020/6/14:16:02
 */
public abstract class AbstractLocaleDomainContainer<L extends ILocaleDomain> extends AbstractSyncSmartList<L> implements ILocaleDomainContainer<L> {
    public AbstractLocaleDomainContainer() {
        super();
    }

    public AbstractLocaleDomainContainer(int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    public boolean add(L l) {
        synchronized (getSyncRoot()) {
            if (super.add(l)) {
                this.merge();
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean remove(L l) {
        synchronized (getSyncRoot()) {
            if (super.remove(l)) {
                this.merge();
                return true;
            }
            return false;
        }
    }

    /**
     * 合并区域化
     * 一般对不同域的区域化进行合并到一个区域化容器中
     * 将区域化合并到下面区域化容器中
     * {@link ILocaleDomainContainer#getLocaleContainer()}
     */
    @Override
    public void merge() {
        //读取锁定
        this.getLocaleContainer().readLock();
        //锁定
        synchronized (this.getLocaleContainer().getSyncRoot()) {
            try {
                //清楚历史区域数据
                this.getLocaleContainer().clear();
                //合并新的区域数据
                for (ILocaleDomain domain : this.getList()) {
                    this.getLocaleContainer().add(domain);
                }
            } finally {
                //读取解锁
                this.getLocaleContainer().readUnLock();
            }
        }
    }
}