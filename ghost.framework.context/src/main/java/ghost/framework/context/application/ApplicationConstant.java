package ghost.framework.context.application;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用常量
 * @Date: 22:54 2019/5/20
 */
public final class ApplicationConstant {
    /**
     * 模块常量前缀
     */
    public static final String PREFIX = "ghost.framework.app";

    /**
     * 文本常量
     */
    public final class Text {
        /**
         * 前缀
         */
        public static final String TEXT_PREFIX = PREFIX + ".text";
        /**
         * 应用编码
         * 默认为null或为UTF-8
         */
        public static final String ENCODING = TEXT_PREFIX + ".encoding";
    }

    /**
     * 绑定类事件
     */
    public static final int ModuleRegistraBean = Integer.MIN_VALUE;
    public static final int ModuleUninstallBean = Integer.MIN_VALUE;
    public static final int ModuleRegistraObjectrInjectionValue = Integer.MIN_VALUE + 1;
    public static final int ModuleRegistraObjectrBean = ModuleRegistraObjectrInjectionValue + 1;
    /**
     * 应用应用临时目录常量
     */
    public static final String TEMP_DIRECTORY = PREFIX + ".temp.directory";
    /**
     * 应用名称键
     */
    public static final String NAME = PREFIX + ".value";
    /**
     * 是否为开发模块键
     * 是为开发模式，否为jar运行模式
     */
    public static final String DEV = PREFIX + ".dev";
    /**
     * 系统类型键
     * win为否，linux为是
     */
    public static final String OS = PREFIX + ".os";

    /**
     * 区域键
     */
    public final class Locale {
        /**
         * 绑定前缀
         */
        public static final String LOCALE_PREFIX = PREFIX + ".locale";
        public static final String I18n = LOCALE_PREFIX + ".i18n";
        public static final String L10n = LOCALE_PREFIX + ".l10n";
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

    /**
     * maven键
     */
    public final class Maven {
        /**
         * maven本地仓库env参数名称
         */
        public static final String LOCAL_REPOSITORY_PATH = PREFIX + ".maven.local.repository.path";
        /**
         *
         */
        public static final String LOCAL_REPOSITORY_URL = PREFIX + ".maven.local.repository.url";
        /**
         * 配置文件maven仓库地址名称常量
         * 格式[value=maven-public,depend=,user=,password=,url=|value=maven-public,depend=,user=,password=,url=]
         */
        public static final String REPOSITORY_URLS = PREFIX + ".maven.repository.urls";
    }
}