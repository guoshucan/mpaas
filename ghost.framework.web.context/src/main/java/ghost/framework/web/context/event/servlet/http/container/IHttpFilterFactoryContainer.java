package ghost.framework.web.context.event.servlet.http.container;

import ghost.framework.context.bean.factory.IBeanFactoryContainer;
import ghost.framework.web.context.event.servlet.http.factory.IHttpFilterFactory;

import java.util.List;
/**
 * package: ghost.framework.web.module.event.servlet.http.container
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 0:57 2020/1/28
 */
public interface IHttpFilterFactoryContainer<L extends Object> extends IBeanFactoryContainer<L> {
    /**
     * 获取HttpServlet工厂列表
     *
     * @return
     */
    List<IHttpFilterFactory> getIHttpFilterFactoryList();

    /**
     * 获取父级接口
     * @return
     */
    IHttpFilterFactoryContainer<L> getParent();
}