package ghost.framework.web.socket.plugin.server;

import ghost.framework.context.module.IModule;
import ghost.framework.web.context.servlet.ServletContextHeader;

import javax.servlet.http.HttpSession;
import javax.websocket.Extension;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * package: ghost.framework.web.socket.plugin.server
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:默认ServerEndpoint配置类
 * @Date: 2020/5/2:18:23
 */
public final class DefaultServerEndpointConfigurator extends ServerEndpointConfig.Configurator {
    private IModule module;
    @Override
    public void modifyHandshake(ServerEndpointConfig config,
                                HandshakeRequest request,
                                HandshakeResponse response) {
        //获取web所在模块
        this.module = (IModule) ServletContextHeader.getServletContext().getAttribute(IModule.class.getName());
        if (request.getHttpSession() != null) {
//            HttpSession httpSession = (HttpSession) request.getHttpSession();
            config.getUserProperties().put(HttpSession.class.getName(), request.getHttpSession());
        }
    }

    /**
     * 获取构建类型实例
     * @param clazz
     * @param <T>
     * @return
     * @throws InstantiationException
     */
    @Override
    public <T> T getEndpointInstance(Class<T> clazz)
            throws InstantiationException {
        try {
            //使用模块Bean构建类型对象
            return module.addBean(clazz);
        } catch (Exception e) {
            throw new InstantiationException(e.getMessage());
        }
    }
    @Override
    public String getNegotiatedSubprotocol(List<String> supported, List<String> requested) {
        for (String request : requested) {
            if (supported.contains(request)) {
                return request;
            }
        }
        return "";
    }
    @Override
    public List<Extension> getNegotiatedExtensions(List<Extension> installed,
                                                   List<Extension> requested) {
        Set<String> installedNames = new HashSet<>();
        for (Extension e : installed) {
            installedNames.add(e.getName());
        }
        List<Extension> result = new ArrayList<>();
        for (Extension request : requested) {
            if (installedNames.contains(request.getName())) {
                result.add(request);
            }
        }
        return result;
    }


    @Override
    public boolean checkOrigin(String originHeaderValue) {
        return true;
    }
}
