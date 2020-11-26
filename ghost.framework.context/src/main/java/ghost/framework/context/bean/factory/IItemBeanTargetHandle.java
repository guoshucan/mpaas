package ghost.framework.context.bean.factory;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:绑定事件目标处理接口
 * @Date: 11:08 2020/1/3
 * @param <O> 发起方类型
 * @param <T> 处理目标类型
 * @param <S> 类型创建绑定名称，可以为空
 */
public interface IItemBeanTargetHandle<O, T, S> extends INameBeanTargetHandle<O, T, S> {
}