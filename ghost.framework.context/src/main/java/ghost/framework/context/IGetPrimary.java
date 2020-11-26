package ghost.framework.context;

/**
 * package: ghost.framework.context.bean
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:获取是否为主接口
 * @Date: 2020/8/10:16:43
 */
public interface IGetPrimary {
    /**
     * 获取是否为主
     * @return
     */
    default boolean isPrimary() {
        return false;
    }
}