package ghost.framework.web.context.event.servlet.http.container;

import ghost.framework.context.bean.factory.IBeanFactoryContainer;
import ghost.framework.web.context.event.servlet.http.factory.IHttpServletFactory;

import java.util.List;

/**
 * package: ghost.framework.web.module.event.servlet.http.container
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/1/27:21:11
 */
public interface IHttpServletFactoryContainer<L extends Object> extends IBeanFactoryContainer<L> {
    /**
     * 获取HttpServlet工厂列表
     *
     * @return
     */
    List<IHttpServletFactory> getHttpServletFactoryList();

    /**
     * 获取父级接口
     * @return
     */
    IHttpServletFactoryContainer<L> getParent();
}