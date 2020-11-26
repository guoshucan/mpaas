package ghost.framework.jetty.ws.module;

import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import java.net.Socket;

/**
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 4:36 2019/11/8
 */
public class WsHandler extends WebSocketHandler {
    @Override
    public void configure(WebSocketServletFactory factory)
    {
        // 设置超时
        factory.getPolicy().setIdleTimeout(10000);

        // 注册
        factory.register(WsListener.class);
        // 因为使用了匿名方式创建WebSocketCreator，这里先将需要初始化类的信息告诉
        // WebSocketServletFactory，这个环节也可以通过预先定制（创建）WebSocketCreator，调用
        // WebSocketCreator.put(Class<?> websocket)的方式告知WebSocketServletFactory
        factory.register(Socket.class);
        final WebSocketCreator creator = factory.getCreator();
        // Set your custom Creator
        factory.setCreator(
            /*(servletUpgradeRequest ,servletUpgradeResponse) -> {
                    Object webSocket =  creator.createWebSocket(servletUpgradeRequest,servletUpgradeResponse);
                    // Use the object created by the default creator and inject your members
                    System.out.println("------------------------------------");
                    injector.injectMembers(webSocket);
                    System.out.println("Injector works complete");
                    return webSocket;
            });*/
                new WebSocketCreator() {
                    @Override
                    public Object createWebSocket(ServletUpgradeRequest servletUpgradeRequest, ServletUpgradeResponse servletUpgradeResponse) {
                        Object webSocket = creator.createWebSocket(servletUpgradeRequest,servletUpgradeResponse);
                        // Use the object created by the default creator and inject your members
                        System.out.println("------------------------------------");
                        //injector.injectMembers(webSocket);
                        System.out.println("Injector works complete");
                        return webSocket;
                    }
                });
    }
}
