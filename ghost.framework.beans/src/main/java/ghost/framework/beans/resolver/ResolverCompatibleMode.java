package ghost.framework.beans.resolver;

/**
 * package: ghost.framework.context.resolver
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:解析兼容模式枚举
 * @Date: 2020/3/18:17:10
 */
public enum ResolverCompatibleMode {
    /**
     * 默认模式
     * 不做任何兼容
     */
    Default,
    /**
     * 忽略空
     */
    IgnoreNull,
    /**
     * 忽略Empty
     */
    IgnoreEmpty,
    /**
     * 忽略null与Empty
     */
    IgnoreNullAndEmpty,
}