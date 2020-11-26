package ghost.framework.web.module.server;

import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.web.context.server.IConfigurableServletWebServerFactoryContainer;
import ghost.framework.web.context.servlet.server.ConfigurableServletWebServerFactory;

import java.util.*;

/**
 * package: ghost.framework.web.module.server
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:配置 {@link ConfigurableServletWebServerFactory} 接口的存储容器
 * @Date: 2020/2/20:16:45
 */
@Component
public class ConfigurableServletWebServerFactoryContainer extends ArrayList<ConfigurableServletWebServerFactory> implements IConfigurableServletWebServerFactoryContainer { }
