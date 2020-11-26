package ghost.framework.web.mvc.plugin.bean.factory;

import ghost.framework.beans.annotation.bean.factory.ClassBeanFactory;
import ghost.framework.context.application.IGetApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.*;
import ghost.framework.context.log.IGetLog;
import ghost.framework.web.context.bens.factory.AbstractWebClassBeanFactory;
import ghost.framework.web.mvc.context.bean.annotation.ClassAnnotationViewResolver;
import ghost.framework.web.mvc.context.bean.factory.IViewResolverInterfaceBeanFactory;
import ghost.framework.web.mvc.context.servlet.view.IViewResolver;
import ghost.framework.web.mvc.context.servlet.view.IViewResolverContainer;

import java.util.Arrays;
/**
 * package: ghost.framework.web.mvc.plugin.bean.factory
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:继承 {@link IViewResolver} 视图解析接口类型绑定工厂类
 * {@link IGetLog}
 * {@link IGetApplication#getApp()}
 * {@link IBeanFactory}
 * {@link IApplicationOwnerBeanFactory}
 * {@link IApplicationExecuteOwnerBeanFactory}
 * {@link IClassBeanFactory}
 * {@link IViewResolverInterfaceBeanFactory}
 * {@link ClassAnnotationViewResolver}
 * {@link IViewResolver}
 * @Date: 2020/6/3:22:10
 */
@ClassBeanFactory(single = true)
public class ViewResolverInterfaceBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassBeanTargetHandle<O, T, V, String, Object>,
                V extends Object
                >
        extends AbstractWebClassBeanFactory<O, T, E, V>
        implements IViewResolverInterfaceBeanFactory<O, T, E, V> {

    @Override
    public String toString() {
        return "ViewResolverInterfaceBeanFactory{" +
                "targetClasss=" + Arrays.toString(targetClasss) +
                '}';
    }


    /**
     * 数组目标类型
     */
    private final Class[] targetClasss = new Class[]{IViewResolver.class};

    /**
     * 获取数组目标类型
     *
     * @return
     */
    @Override
    public Class[] getTargetClasss() {
        return targetClasss;
    }

    /**
     * 加载类型
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        this.getLog().info("loader:" + event.getTarget().getName());
        //判断是否使用代理构建类型
        if (this.isProxyInstance(event)) {
            //代理构建类型
            this.newInstance(event);
        } else {
            //不使用代理构建类型
            this.newInstance(event);
        }
        //将构建完成的mvc视图解析器添加入视图解析器容器中
        this.getWebModule().getBean(IViewResolverContainer.class).add(event.getValue());
    }

    @Override
    public void unloader(E event) {
        this.getLog().info("unloader:" + event.getTarget().getName());
    }
}