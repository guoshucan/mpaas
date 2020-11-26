package ghost.framework.core.bean.factory.scan;

import ghost.framework.beans.annotation.scan.ScanResourceEventFactory;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.annotation.IAnnotationExecutionChain;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.AbstractClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.core.annotation.AnnotationRootExecutionChain;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * package: ghost.framework.core.bean.factory.scan
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/23:14:23
 */
@Component
public class DefaultScanResourceBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends Object,
                AK extends Class<? extends Annotation>,
                AV extends IAnnotationExecutionChain<AK, AV, AL>,
                AL extends List<AV>
                >
        extends AbstractClassAnnotationBeanFactory<O, T, E, V>
        implements IClassAnnotationBeanFactory<O, T, E, V> {
    @Override
    public String toString() {
        return "DefaultScanResourceBeanFactory{" +
                "annotation=" + annotation +
                '}';
    }

    /**
     * 事件工厂注释
     */
    private final Class<? extends Annotation> annotation = ScanResourceEventFactory.class;

    /**
     * 获取绑定注释类型
     *
     * @return
     */
    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return annotation;
    }

    @Override
    public void loader(E event) {
        //获取容器注释
        ScanResourceEventFactory scanResourceEventFactory = this.getAnnotation(event);
        //获取注释链接口
        AnnotationRootExecutionChain<AK, AV, AL> chainMap = this.getApp().getBean(AnnotationRootExecutionChain.class);
        //获取类型注释
        IClassAnnotationBeanFactory eventFactory = (IClassAnnotationBeanFactory) event.getValue();
        //添加注释事件工厂注释类型
        AV av = chainMap.add((AK) eventFactory.getAnnotationClass(), scanResourceEventFactory.single(), scanResourceEventFactory.tag(), event.getExecuteOwner());
    }

    @Override
    public void unloader(E event) {

    }
}