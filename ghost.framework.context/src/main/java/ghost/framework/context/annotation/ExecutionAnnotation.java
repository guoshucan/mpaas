package ghost.framework.context.annotation;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Objects;

/**
 * package: ghost.framework.context.annotation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:执行注释
 * @Date: 2020/2/7:11:31
 */
public class ExecutionAnnotation
        <
                K extends Class<? extends Annotation>,
                V extends IAnnotationExecutionChain<K, V, L>,
                L extends Collection<V>
        > {
    /**
     * 初始化执行注释
     * @param executionChain 注释执行链对象
     */
    public ExecutionAnnotation(IAnnotationExecutionChain<K, V, L> executionChain) {
        this.executionChain = executionChain;
    }

    @Override
    public String toString() {
        return "ExecutionAnnotation{" +
                "executionChain=" + executionChain.getAnnotationClass().toString() +
                ", execute=" + execute +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExecutionAnnotation<?, ?, ?> that = (ExecutionAnnotation<?, ?, ?>) o;
        return execute == that.execute &&
                Objects.equals(executionChain.getAnnotationClass(), that.executionChain.getAnnotationClass());
    }

    @Override
    public int hashCode() {
        return Objects.hash(executionChain.getAnnotationClass(), execute);
    }

    public K getAnnotationClass() {
        return executionChain.getAnnotationClass();
    }

    /**
     * 执行链
     */
    private IAnnotationExecutionChain<K, V, L> executionChain;

    /**
     * 获取执行链
     * @return
     */
    public IAnnotationExecutionChain<K, V, L> getExecutionChain() {
        return executionChain;
    }

    /**
     * 是否已经执行
     */
    private boolean execute;

    /**
     * 设置是否已经执行
     *
     * @param execute 是否已经执行
     */
    public void setExecute(boolean execute) {
        this.execute = execute;
    }

    /**
     * 获取是否已经执行
     *
     * @return
     */
    public boolean isExecute() {
        return execute;
    }
}