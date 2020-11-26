package ghost.framework.context.module.main;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块主运行类容器接口
 * @Date: 20:28 2019/12/18
 */
public interface IModuleMainContainer {
    void remove(Object main);

    boolean contains(Object main);

    void add(Object main);
}