package ghost.framework.bus;

import ghost.framework.context.environment.IEnvironment;

/**
 * package: ghost.framework.bus.content
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:总线服务容器接口
 * {@link AutoCloseable#close()}
 * {@link IBusServer#getStatus()}
 * @Date: 2020/6/12:20:13
 */
public interface IBusServer extends AutoCloseable{
    /**
     * 关闭
     * @throws Exception
     */
    @Override
    void close() throws Exception;

    /**
     * 启动
     */
    void start();
    /**
     * 获取总线服务状态
     * @return
     */
    BusStatus getStatus();

    /**
     * 获取应用env
     * @return
     */
    IEnvironment getEnv();
}
