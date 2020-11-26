package ghost.framework.context.bean.factory;

import ghost.framework.context.application.IApplication;
import ghost.framework.context.application.IApplicationEnvironment;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.environment.IEnvironment;
import ghost.framework.context.exception.AnnotationException;
import ghost.framework.context.module.environment.IModuleEnvironment;
import ghost.framework.context.proxy.ProxyUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * package: ghost.framework.context.bean.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注释事件工厂接口
 * @Date: 18:10 2020/1/20
 * @param <O> 发起方核心接口
 * @param <T> 目标类型
 * @param <E> 注释事件目标处理接口
 */
public interface IAnnotationBeanFactory
        <
        O extends ICoreInterface,
        T,
        E extends IAnnotationBeanTargetHandle<O, T>
        >
        extends IApplicationExecuteOwnerBeanFactory<O, T, E> {

    /**
     * 是否已经加载
     *
     * @param event 事件对象
     * @return
     */
    default boolean isLoader(E event) {
        //判断该注释是否已经执行了
        if (event.getExecutionAnnotationChain().containsKey(this.getAnnotationClass()) && event.getExecutionAnnotationChain().get(this.getAnnotationClass()).isExecute()) {
            //此注释已经执行了
            return true;
        }
        //此注释未执行
        return false;
    }
    /**
     * 获取拥有者的env接口
     * 此函数必须在调用positionOwner函数定位好拥有者之后
     *
     * @param event 事件对象
     * @return
     */
    default IEnvironment getEnvironment(E event) {
        //判断env拥有者类型
        if (event.getExecuteOwner() instanceof IApplication) {
            //获取应用env
            return event.getExecuteOwner().getBean(IApplicationEnvironment.class);
        }
        //获取模块env
        return event.getExecuteOwner().getBean(IModuleEnvironment.class);
    }

    /**
     * 已经执行
     *
     * @param event 事件对象
     */
    default void setExecute(E event) {
        //设置当前注释事件工厂已经执行
        if (event.getExecutionAnnotationChain() == null || event.getExecutionAnnotationChain().isEmpty()) {
            return;
        }
        if (event.getExecutionAnnotationChain().containsKey(this.getAnnotationClass())) {
            event.getExecutionAnnotationChain().get(this.getAnnotationClass()).setExecute(true);
        }
        //遍历所有注释执行链是否已经完成
        //全部注释执行链完成，设置处理标志
        event.checkExecutionAnnotationChainCompleted();
    }

    /**
     * 获取绑定注释类型
     *
     * @return
     */
    default Class<? extends Annotation> getAnnotationClass() {
        throw new UnsupportedOperationException(IAnnotationBeanFactory.class.getName() + "#getAnnotationClass()");
    }

    /**
     * 获取注释对象
     * 当在事件的Target目标处理对象获取不到注释类型原始对象时在注释链获取该注释原始对象
     * 通过 {@link IAnnotationBeanFactory ::getAnnotationClass()} 该注释类型获取注释源对象
     *
     * @param event 事件对象
     * @param <R>
     * @return 返回Target目标注释类型，如果Target目标注释类型不存在将在注释执行链获取原始注释对象
     */
    default <R extends Annotation> R getAnnotation(E event) {
        throw new UnsupportedOperationException(IAnnotationBeanFactory.class.getName() + "#getAnnotation(event)");
    }

    /**
     * 查找注释对象
     *
     * @param root 类型的数组注释
     * @return
     */
    default Annotation forEachAnnotation(Annotation[] root) {
        //初始化注释判断函数
        AnnotationPredicate<Annotation> predicate = new AnnotationPredicate<Annotation>() {
            @Override
            public boolean test(Annotation a) {
                Class<? extends Annotation> c = ProxyUtil.getProxyObjectAnnotationClass(a);
                if (this.classList.contains(c)) {
                    return false;
                }
                this.classList.add(c);
                //判断是否为当前注释对象
                if (c.equals(getAnnotationClass())) {
                    this.a = a;
                    //找到注释对象，返回true不再遍历查找注释
                    return true;
                }
                return Arrays.asList(a.annotationType().getDeclaredAnnotations()).stream().anyMatch(this::test);
            }
        };
        //找到注释对象
        if (!Arrays.asList(root).stream().anyMatch(predicate)) {
            //未找到注释对象错误
            throw new AnnotationException(this.getAnnotationClass().toString());
        }
        return predicate.getA();
    }

    /**
     * 注释判断函数
     *
     * @param <P>
     */
    abstract class AnnotationPredicate<P extends Annotation> implements Predicate<P> {
        protected P a;

        public P getA() {
            return a;
        }

        /**
         * 存放已经处理的注释类型列表
         */
        protected List<Class<? extends Annotation>> classList = new ArrayList<>();
    }
}