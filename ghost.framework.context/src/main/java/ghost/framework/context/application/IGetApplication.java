package ghost.framework.context.application;

/**
 * package: ghost.framework.context.app
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:获取拥有接口
 * @Date: 2019/12/11:21:14
 */
public interface IGetApplication {
    /**
     * 获取应用接口
     * @return
     */
    default IApplication getApp() {
        return (IApplication) this;
    }
}
