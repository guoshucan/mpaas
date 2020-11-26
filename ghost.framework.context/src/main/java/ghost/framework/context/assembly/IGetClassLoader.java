package ghost.framework.context.assembly;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类加载器接口
 * @Date: 10:07 2019/12/15
 */
public interface IGetClassLoader {
    /**
     * 获取类加载器
     * @return
     */
    IClassLoader getClassLoader();
}