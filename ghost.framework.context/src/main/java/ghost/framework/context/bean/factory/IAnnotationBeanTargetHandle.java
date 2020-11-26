package ghost.framework.context.bean.factory;

import ghost.framework.beans.annotation.ParentPriority;
import ghost.framework.context.annotation.ExecutionAnnotation;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * package: ghost.framework.core.event.annotation
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注释目标处理接口
 * @Date: 11:16 2020/2/1
 */
public interface IAnnotationBeanTargetHandle<O, T> extends IExecuteOwnerBeanTargetHandle<O, T> {
    /**
     * 设置执行注释链
     *
     * @param executionAnnotationChain 执行注释链
     */
    void setExecutionAnnotationChain(Map<Class<? extends Annotation>, ExecutionAnnotation> executionAnnotationChain);

    /**
     * 获取执行注释链
     *
     * @return 返回执行注释链
     */
    Map<Class<? extends Annotation>, ExecutionAnnotation> getExecutionAnnotationChain();

    /**
     * 检查执行注释链是否完成
     * 如果全部注释执行链都执行完成时设置 {@link IAnnotationBeanTargetHandle ::setHandle} 参数为true
     */
    default void checkExecutionAnnotationChainCompleted() {
        this.setHandle(this.getExecutionAnnotationChain().entrySet().stream().allMatch(a -> a.getValue().isExecute() == true));
    }

    /**
     * 获取父及优先注释
     *
     * @return
     */
    ParentPriority getParentPriority();

    /**
     * 获取是否父级优先
     *
     * @return
     */
    default boolean isParentPriority() {
        return this.getParentPriority() != null;
    }
}