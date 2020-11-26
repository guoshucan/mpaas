package ghost.framework.core.event.method.container;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.context.annotation.ExecutionAnnotation;
import ghost.framework.context.annotation.IAnnotationRootExecutionChain;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.BeanMethod;
import ghost.framework.context.bean.utils.BeanUtil;
import ghost.framework.context.bean.factory.IMethodBeanTargetHandle;
import ghost.framework.context.bean.factory.method.MethodAnnotationBeanFactoryContainer;
import ghost.framework.context.bean.factory.method.MethodAnnotationBeanFactory;
import ghost.framework.context.bean.factory.AbstractBeanFactoryContainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:操作函数列表容器事件工厂容器类
 * @Date: 19:01 2020/1/14
 * @param <O> 发起方类型
 * @param <T> 绑定定义类型
 * @param <E> 操作绑定事件目标处理类型
 */
public class DefaultMethodAnnotationBeanFactoryContainer
        <
                O extends ICoreInterface,
                T extends Object,
                E extends IMethodBeanTargetHandle<O, T, Method>,
                L extends MethodAnnotationBeanFactory<O, T, E>
                >
        extends AbstractBeanFactoryContainer<L>
        implements MethodAnnotationBeanFactoryContainer<O, T, E, L> {
    /**
     * 初始化类事件监听容器
     *
     * @param parent 父级类事件监听容器
     */
    public DefaultMethodAnnotationBeanFactoryContainer(@Autowired IApplication app, @Application @Autowired @Nullable MethodAnnotationBeanFactoryContainer<O, T, E, L> parent) {
        this.app = app;
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "DefaultMethodAnnotationBeanFactoryContainer{" +
                "app=" + app.toString() +
                ", parent=" + (parent == null ? "" : parent.toString()) +
                '}';
    }

    /**
     * 应用接口
     */
    private IApplication app;

    /**
     * 父级类事件监听容器
     */
    private MethodAnnotationBeanFactoryContainer<O, T, E, L> parent;

    /**
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        //遍历函数列表
        for (BeanMethod method : BeanUtil.getBeanMethodOrderList(event.getTarget().getClass())) {
            //设置动作函数
            event.setMethod(method.getMethod());
            //获取函数注释链
            event.setExecutionAnnotationChain(this.app.getBean(IAnnotationRootExecutionChain.class).getExecutionChain(event.getMethod()));
            //不为配置注释类型时删除 Bean 标签链
            if (!event.getTarget().getClass().isAnnotationPresent(Configuration.class)) {
                List<Class<? extends Annotation>> list = new ArrayList<>();
                event.getExecutionAnnotationChain().forEach(new BiConsumer<Class<? extends Annotation>, ExecutionAnnotation>() {
                    @Override
                    public void accept(Class<? extends Annotation> aClass, ExecutionAnnotation executionAnnotation) {
                        if (executionAnnotation.getExecutionChain().getTag().equals(AnnotationTag.AnnotationTags.Bean)) {
                            list.add(aClass);
                        }
                    }
                });
                list.forEach(event.getExecutionAnnotationChain()::remove);
            }
            //判断是否有效执行注释链列表
            if (event.getExecutionAnnotationChain().isEmpty()) {
                continue;
            }
            this.getLog().info("loader:" + event.toString());
            //遍历事件监听容器列表
            for (Map.Entry<Class<? extends Annotation>, ExecutionAnnotation> entry : new HashMap<>(event.getExecutionAnnotationChain()).entrySet()) {
                //遍历事件工厂
                for (MethodAnnotationBeanFactory<O, T, E> factory : new ArrayList<>(this)) {
                    //判断事件工厂注释
                    if (factory.getAnnotationClass().equals(entry.getKey()) && !factory.isLoader(event)) {
                        factory.positionOwner(event);
                        factory.loader(event);
                        factory.setExecute(event);
                    }
                }
                //判断注释是否没有工厂处理
                ExecutionAnnotation executionAnnotation = event.getExecutionAnnotationChain().get(entry.getKey());
                if (!executionAnnotation.isExecute()) {
                    this.getLog().warn("loader " + event.getTarget().getClass().getName() + " in method " + method.getMethod().getName() + " " + entry.getKey().toString().replace("interface ", "annotation ") + " not event factory execute！");
                    executionAnnotation.setExecute(true);
                }
            }
            event.checkExecutionAnnotationChainCompleted();
            //重置状态
            if (event.isHandle()) {
                //重置
                event.setHandle(false);
            } else {
                //执行父级
                if (this.parent != null) {
                    this.parent.loader(event);
                }
            }
        }
    }

    @Override
    public void unloader(E event) {

    }
}