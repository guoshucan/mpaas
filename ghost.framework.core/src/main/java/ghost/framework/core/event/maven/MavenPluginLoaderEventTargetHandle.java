package ghost.framework.core.event.maven;

/**
 * package: ghost.framework.core.event.maven
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:maven插件加载事件目标处理类
 * @Date: 2020/2/4:13:40
 */
public class MavenPluginLoaderEventTargetHandle<O, T> extends MavenEventTargetHandle<O, T> implements ghost.framework.context.event.maven.IMavenEventTargetHandle<O, T> {
    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     */
    public MavenPluginLoaderEventTargetHandle(O owner, T target) {
        super(owner, target);
    }
}
