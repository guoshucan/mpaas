package ghost.framework.context.io;

/**
 * package: ghost.framework.context.io
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:资源类型加载器接口
 * @Date: 2020/6/13:23:10
 */
public interface IResourceClassLoader {

    /**
     * 资源所属类加载器
     */
    default ClassLoader getClassLoader() {
        return null;
    }
}
