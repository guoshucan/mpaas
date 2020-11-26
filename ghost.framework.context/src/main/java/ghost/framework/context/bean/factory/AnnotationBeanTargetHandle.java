package ghost.framework.context.bean.factory;

import ghost.framework.beans.annotation.ParentPriority;
import ghost.framework.context.annotation.ExecutionAnnotation;
import ghost.framework.context.maven.ExecuteOwnerBeanTargetHandle;

import java.lang.annotation.Annotation;
import java.util.Map;
/**
 * package: ghost.framework.core.event.annotation
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注释目标处理基础类
 * @Date: 11:16 2020/2/1
 */
public class AnnotationBeanTargetHandle<O, T> extends ExecuteOwnerBeanTargetHandle<O, T> implements IAnnotationBeanTargetHandle<O, T> {
    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     */
    public AnnotationBeanTargetHandle(O owner, T target) {
        super(owner, target);
    }
    /**
     * 设置执行注释链
     * @param executionAnnotationChain 执行注释链
     */
    @Override
    public void setExecutionAnnotationChain(Map<Class<? extends Annotation>, ExecutionAnnotation> executionAnnotationChain) {
        this.executionAnnotationChain = executionAnnotationChain;
    }

    /**
     * 注释执行链
     */
    private Map<Class<? extends Annotation>, ExecutionAnnotation> executionAnnotationChain;
    /**
     * 获取执行注释链
     * @return 返回执行注释链
     */
    @Override
    public Map<Class<? extends Annotation>, ExecutionAnnotation> getExecutionAnnotationChain() {
        return executionAnnotationChain;
    }

    @Override
    public ParentPriority getParentPriority() {
        throw new UnsupportedOperationException("getParentPriority");
    }
}