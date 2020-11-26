package ghost.framework.context;

/**
 * package: ghost.framework.context
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/4:23:09
 */
public final class CommonConstant {
    /**
     * 模块常量前缀
     */
    public static final String PREFIX = "ghost.framework";
    /**
     * 模块常量前缀
     */
    public static final String COMMON = PREFIX + ".common";

    /**
     * maven键
     */
    public final class Maven {
        /**
         * maven插件常量
         */
//        public static final String ARTIFACT_PLUGINS = COMMON + ".maven.artifact.plugins";
    }

    /**
     * ghost.framework.profiles.active
     * 配置常量
     */
    public final class Profiles {
        public static final String PROFILES_PREFIX = PREFIX + ".profiles";
        public static final String ACTIVE = PROFILES_PREFIX + ".active";
    }
}