package ghost.framework.context.bean.factory;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:绑定事件目标处理接口
 * @Date: 11:08 2020/1/3
 */
public interface INameBeanTargetHandle<O, T, S> extends IExecuteOwnerBeanTargetHandle<O, T> {
    /**
     * 获取绑定名称
     * @return
     */
    S getName();

    /**
     * 设置绑定名称
     * @param name
     */
    void setName(S name);
}
