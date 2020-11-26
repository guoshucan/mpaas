package ghost.framework.context.bean.factory;

import ghost.framework.context.log.IGetLog;

import java.util.Collection;
/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:全部事件工厂容器基础接口
 * @Date: 23:41 2019/12/21
 * @param <L> 事件监听容器接口
 */
public interface IBeanFactoryContainer<L> extends AutoCloseable, Collection<L>, IGetLog {
}