package ghost.framework.web.context.servlet;

import javax.servlet.ServletRequestListener;
import java.util.Collection;

/**
 * package: ghost.framework.web.context.servlet
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/2:14:09
 */
public interface IServletRequestListenerContainer extends Collection<ServletRequestListener>, ServletRequestListener, IServletContextListenerContainerBase{
}
