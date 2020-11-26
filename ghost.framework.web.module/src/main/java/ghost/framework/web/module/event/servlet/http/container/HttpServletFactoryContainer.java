package ghost.framework.web.module.event.servlet.http.container;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.invoke.Invoke;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.bean.factory.AbstractBeanFactoryContainer;
import ghost.framework.context.module.IModule;
import ghost.framework.web.context.event.servlet.http.container.IHttpServletFactoryContainer;
import ghost.framework.web.context.event.servlet.http.factory.IHttpServletFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * package: ghost.framework.web.module.event.servlet.http.container
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/1/27:22:01
 */
@Component
public class HttpServletFactoryContainer<L extends Object>
        extends AbstractBeanFactoryContainer<L>
        implements IHttpServletFactoryContainer<L> {
    /**
     * @param parent
     */
    public HttpServletFactoryContainer(@Application @Autowired @Nullable IHttpServletFactoryContainer<L> parent) {
        this.parent = parent;
        if (parent == null) {
            this.getLog().info("~" + this.getClass().getName());
        } else {
            this.getLog().info("~" + this.getClass().getName() + "(parent:" + parent.getClass().getName() + ")");
        }
    }
    /**
     * 应用接口
     */
    @Autowired
    private IApplication app;

    /**
     * 初始化处理
     */
    @Invoke
    public void init(){

    }
    /**
     * 注入模块接口
     */
    @Autowired
    private IModule module;
    /**
     * 父级接口
     */
    private IHttpServletFactoryContainer<L> parent;

    /**
     * 获取父级接口
     *
     * @return
     */
    @Override
    public IHttpServletFactoryContainer<L> getParent() {
        return parent;
    }

    /**
     * 获取HttpServlet工厂列表
     *
     * @return
     */
    @Override
    public List<IHttpServletFactory> getHttpServletFactoryList() {
        List<IHttpServletFactory> list = new ArrayList<>();
        //累加父级
        if (this.parent != null) {
            list.addAll(this.parent.getHttpServletFactoryList());
        }
        //累加级
        for (L l : this) {
            if (l instanceof Class) {
                list.add((IHttpServletFactory) this.module.getBean((Class<?>) l));
                continue;
            }
            if (IHttpServletFactory.class.isAssignableFrom(l.getClass())) {
                list.add((IHttpServletFactory) l);
            }
        }
        return list;
    }
}