package ghost.framework.context.locale;

import ghost.framework.beans.annotation.module.ModulePackage;
import ghost.framework.context.collections.generic.ISmartList;
import ghost.framework.context.thread.GetSyncRoot;

/**
 * package: ghost.framework.context.locale
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:区域域容器接口
 * 作为下面包注释的国际化域容器
 * {@see PluginPackage}
 * {@link ModulePackage}
 * @Date: 2020/6/14:16:00
 */
public interface ILocaleDomainContainer<L extends ILocaleDomain> extends GetSyncRoot, ISmartList<L> {
    /**
     * 合并区域化
     * 一般对不同域的区域化进行合并到一个区域化容器中
     * 将区域化合并到下面区域化容器中
     * {@link ILocaleDomainContainer#getLocaleContainer()}
     */
    default void merge() {
        throw new UnsupportedOperationException(ILocaleDomainContainer.class.getName() + "#merge()");
    }

    /**
     * 获取区域容器接口
     *
     * @return 返回区域容器接口
     */
    default ILocaleContainer getLocaleContainer() {
        throw new UnsupportedOperationException(ILocaleDomainContainer.class.getName() + "#getLocaleContainer()");
    }

    /**
     * 获取域区域域接口
     *
     * @param domain
     * @return
     */
    default ILocaleDomain get(Object domain) {
        synchronized (getSyncRoot()) {
            for (ILocaleDomain d : this.getList()) {
                if (d.getDomain().equals(domain)) {
                    return d;
                }
            }
        }
        return null;
    }
}