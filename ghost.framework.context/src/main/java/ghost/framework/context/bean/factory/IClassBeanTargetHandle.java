package ghost.framework.context.bean.factory;
import ghost.framework.beans.annotation.ParentPriority;
/**
 * package: ghost.framework.context.bean.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注释目标处理接口
 * @Date: 11:16 2020/2/1
 */
public interface IClassBeanTargetHandle
        <O, T, V, S, P>
        extends IExecuteOwnerBeanTargetHandle<O, T>,
        IItemBeanTargetHandle<O, T, S>,
        INameBeanTargetHandle<O, T, S>,
        IValueBeanTargetHandle<O, T, V>,
        IParametersBeanTargetHandle<O, T, P>,
        INameValueParametersBeanTargetHandle<O, T, S, V, P> {

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