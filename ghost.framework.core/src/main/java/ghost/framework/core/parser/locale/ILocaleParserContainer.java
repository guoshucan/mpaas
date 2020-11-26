package ghost.framework.core.parser.locale;

import java.util.List;

/**
 * package: ghost.framework.core.parser.locale
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:区域化解析容器接口
 * @Date: 2020/1/21:22:16
 */
public interface ILocaleParserContainer<T extends ILocaleParser> extends List<T>, ILocaleParser {
}