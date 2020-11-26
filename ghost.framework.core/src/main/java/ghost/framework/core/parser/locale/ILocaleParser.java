package ghost.framework.core.parser.locale;

import ghost.framework.context.locale.ILocaleContainer;
import ghost.framework.core.parser.IParser;

import java.net.URL;

/**
 * package: ghost.framework.core.parser.locale
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:区域化解析器接口
 * @Date: 2020/1/21:22:04
 */
public interface ILocaleParser extends IParser {
    /**
     * 加载区域资源信息
     *
     * @param url          包url
     * @param container    语言容器
     * @param resourceName 语言资源目录名称
     * @param dev 拥有者是否为开发模式
     */
    void loader(URL url, ILocaleContainer container, String resourceName, boolean dev);
}