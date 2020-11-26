package ghost.framework.context.module;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块常量
 * @Date: 21:03 2019/12/18
 */
public final class ModuleConstant {
    /**
     * 模块常量前缀
     */
    public static final String PREFIX = "ghost.framework.module";
//    /**
//     * 模块app键
//     */
//    public static final String MODULE_APPLICATION = PREFIX + ".ModuleApplication";
    /**
     * 模块名称键
     */
    public static final String NAME = PREFIX + ".name";
    /**
     * 模块目录键
     */
    public static final String HOME = PREFIX + ".home";
    /**
     * 模块版本键
     */
    public static final String VERSION = PREFIX + ".version";
    /**
     * 模块排序键
     */
    public static final String ORDER = PREFIX + ".order";
    /**
     * 模块日志键
     */
    public static final String LOG = PREFIX + ".log";
    /**
     * 模块临时目录键
     */
    public static final String TEMP_DIRECTORY = PREFIX + ".temp.directory";
    /**
     * 模块开发模式键
     */
    public static final String DEV = PREFIX + ".dev";
    /**
     * 文本常量
     */
    public final class Text {
        /**
         * 前缀
         */
        public static final String TEXT_PREFIX = PREFIX + ".text";
        /**
         * 模块编码
         * 默认为null或为UTF-8
         */
        public static final String ENCODING = TEXT_PREFIX + ".encoding";
    }
    /**
     * 绑定键
     */
    public final class Bean {
        /**
         * 绑定前缀
         */
        public static final String BEAN_PREFIX = PREFIX + ".bean";
        /**
         * 是否为自动绑定键，值为布尔值
         */
        public static final String AUTO_BEAN = BEAN_PREFIX + ".auto.bean";
    }
}