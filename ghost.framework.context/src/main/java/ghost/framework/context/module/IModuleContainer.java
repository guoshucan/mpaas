package ghost.framework.context.module;

import ghost.framework.context.event.container.IEventFactoryContainer;

import java.util.List;
import java.util.Map;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块容器接口
 * @Date: 1:37 2019-05-28
 */
public interface IModuleContainer extends Map<String, IModule>, IGetModule, IContainsModule, IRemoveModule {
    /**
     * 获取全部模块事件监听容器接口列表
     * @return
     */
    List<IEventFactoryContainer> getEventListenerContainerList();
}