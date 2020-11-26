package ghost.framework.core.parser.annotation;

import ghost.framework.context.bean.factory.IBeanFactory;

/**
 * package: ghost.framework.core.parser.annotation
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型注释解析器接口
 * @Date: 15:24 2020/2/2
 */
public interface IClassAnnotationParser<E> extends IAnnotationParser{
    /**
     * 加载事件
     * @param eventFactory 事件工厂
     * @param event 事件对象
     */
    void loader(IBeanFactory eventFactory, E event);
}
