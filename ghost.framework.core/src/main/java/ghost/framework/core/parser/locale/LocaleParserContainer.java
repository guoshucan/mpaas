package ghost.framework.core.parser.locale;

import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.locale.ILocaleContainer;

import java.net.URL;
import java.util.ArrayList;

/**
 * package: ghost.framework.core.parser.locale
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:区域化解析器容类
 * @Date: 2020/1/21:22:16
 */
@Component
public class LocaleParserContainer<T extends ILocaleParser> extends ArrayList<T> implements ILocaleParserContainer<T> {
    /**
     * 加载区域资源信息
     *
     * @param url          包url
     * @param container    语言容器
     * @param resourceName 语言资源目录名称
     * @param dev 拥有者是否为开发模式
     */
    @Override
    public void loader(URL url, ILocaleContainer container, String resourceName, boolean dev) {
        //遍历区域化解析器
        for (T t : new ArrayList<>(this)) {
            t.loader(url, container, resourceName, dev);
        }
    }
}