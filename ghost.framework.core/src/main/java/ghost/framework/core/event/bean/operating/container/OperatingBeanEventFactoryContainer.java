package ghost.framework.core.event.bean.operating.container;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.annotation.ExecutionAnnotation;
import ghost.framework.context.annotation.IAnnotationRootExecutionChain;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.bean.factory.IOperatingBeanBeanTargetHandle;
import ghost.framework.core.event.bean.operating.factory.IOperatingBeanAnnotationEventFactory;
import ghost.framework.context.bean.factory.AbstractBeanFactoryContainer;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Map;

/**
 * package: ghost.framework.core.event.bean.factory
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:操作绑定事件监听工厂容器类
 * @Date: 2020/1/13:13:20
 * @param <O> 发起方类型
 * @param <T> 绑定定义接口类型
 * @param <L> 容器对象类型，支持类型与对象两种模式处理
 * @param <E> 事件目标处理类型
 */
public class OperatingBeanEventFactoryContainer
        <
                O extends ICoreInterface,
                T extends IBeanDefinition,
                E extends IOperatingBeanBeanTargetHandle<O, T>,
                L extends IOperatingBeanAnnotationEventFactory<O, T, E>
                >
        extends AbstractBeanFactoryContainer<L>
        implements IOperatingBeanEventFactoryContainer<O, T, E, L> {
    /**
     * 初始化类事件监听容器
     *
     * @param parent 父级类事件监听容器
     */
    public OperatingBeanEventFactoryContainer(@Autowired IApplication app, @Application @Autowired @Nullable IOperatingBeanEventFactoryContainer<O, T, E, L> parent) {
        this.app = app;
        this.parent = parent;
    }

    private IApplication app;

    /**
     * 获取父级
     *
     * @return
     */
    @Override
    public IOperatingBeanEventFactoryContainer<O, T, E, L> getParent() {
        return parent;
    }

    /**
     * 父级类事件监听容器
     */
    private IOperatingBeanEventFactoryContainer<O, T, E, L> parent;
    /**
     * 添加绑定前事件
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        //向父级加载
        if (event.isParentPriority() && this.parent != null) {
            //向父级别处理未执行的注释
            this.parent.loader(event);
            if (event.isHandle()) {
                return;
            }
        }
        event.setExecutionAnnotationChain(this.app.getBean(IAnnotationRootExecutionChain.class).getExecutionChain(event.getTarget().getObject().getClass().getDeclaredAnnotations()));
        if (event.getExecutionAnnotationChain().isEmpty()) {
            return;
        }
        this.getLog().info("loader:" + event.getTarget().getObject().toString());
        for (Map.Entry<Class<? extends Annotation>, ExecutionAnnotation> entry : event.getExecutionAnnotationChain().entrySet()) {
            for (IOperatingBeanAnnotationEventFactory<O, T, E> factory : new ArrayList<>(this)) {
                if (factory.getAnnotationClass().equals(entry.getKey()) && !factory.isLoader(event)) {
                    factory.positionOwner(event);
                    factory.loader(event);
                    factory.setExecute(event);
                    break;
                }
            }
            //判断注释是否没有工厂处理
            ExecutionAnnotation executionAnnotation = event.getExecutionAnnotationChain().get(entry.getKey());
            if (!executionAnnotation.isExecute()) {
                this.getLog().warn("loader " + event.getTarget().getObject().getClass().getName() + " in " + entry.getKey().toString().replace("interface ", "annotation ") + " not event factory execute！");
                executionAnnotation.setExecute(true);
            }
        }
        event.checkExecutionAnnotationChainCompleted();
        //判断是否已经处理
        if (event.isHandle()) {
            //退出不向父级执行
            return;
        }
        //向父级加载
        if (!event.isParentPriority() && this.parent != null) {
            //向父级别处理未执行的注释
            this.parent.loader(event);
        }
    }

    @Override
    public void unloader(E event) {

    }
}