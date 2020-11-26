package ghost.framework.web.module.event.servlet.http.container;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.AbstractBeanFactoryContainer;
import ghost.framework.web.context.event.servlet.http.container.IHttpFilterFactoryContainer;
import ghost.framework.web.context.event.servlet.http.factory.IHttpFilterFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * package: ghost.framework.web.module.event.servlet.http.container
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 0:57 2020/1/28
 */
@Component
public class HttpFilterFactoryContainer<L extends Object>
        extends AbstractBeanFactoryContainer<L>
        implements IHttpFilterFactoryContainer<L> {
    /**
     * @param parent
     */
    public HttpFilterFactoryContainer(@Application @Autowired @Nullable IHttpFilterFactoryContainer<L> parent) {
        this.parent = parent;
        if (parent == null) {
            this.getLog().info("~" + this.getClass().getName());
        } else {
            this.getLog().info("~" + this.getClass().getName() + "(parent:" + parent.getClass().getName() + ")");
        }
    }
//    /**
//     * 应用接口
//     */
//    @Autowired
//    private IApplication app;
//    /**
//     * 初始化处理
//     */
//    @ConditionalOnMissingClass
//    @Application
//    public IHttpFilterFactoryContainer<L> init(){
//
//    }
    /**
     * 注入模块接口
     */
    @Autowired
    private ICoreInterface module;
    /**
     * 父级接口
     */
    private IHttpFilterFactoryContainer<L> parent;

    /**
     * 获取HttpServlet工厂列表
     *
     * @return
     */
    @Override
    public List<IHttpFilterFactory> getIHttpFilterFactoryList() {
        List<IHttpFilterFactory> list = new ArrayList<>();
        //累加父级
        if (this.parent != null) {
            list.addAll(this.parent.getIHttpFilterFactoryList());
        }
        //累加级
        for (L l : this) {
            if (l instanceof Class) {
                list.add((IHttpFilterFactory) this.module.getBean((Class<?>) l));
                continue;
            }
            if (IHttpFilterFactory.class.isAssignableFrom(l.getClass())) {
                list.add((IHttpFilterFactory) l);
            }
        }
        return list;
    }

    /**
     * 获取父级接口
     *
     * @return
     */
    @Override
    public IHttpFilterFactoryContainer<L> getParent() {
        return parent;
    }
}