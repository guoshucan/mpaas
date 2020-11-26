package ghost.framework.core.locale;
import ghost.framework.beans.annotation.constructor.Constructor;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.locale.IL10nContainer;
/**
 * package: ghost.framework.core.locale
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:区域本地化容器
 * i10n
 * {@link LocaleContainer}
 * {@link IL10nContainer}
 * @Date: 19:15 2020/1/20
 */
@Component
public final class L10nContainer extends LocaleContainer implements IL10nContainer {
    /**
     * 注释构建入口
     * @param domain
     */
    @Constructor
    public L10nContainer(ICoreInterface domain) {
        this.domain = domain;
    }

    /**
     * 构建入口
     * @param domain
     */
    public L10nContainer(Object domain) {
        this.domain = domain;
    }

    /**
     * 获取所属域
     * @return
     */
    @Override
    public Object getDomain() {
        return domain;
    }

    private final Object domain;
}