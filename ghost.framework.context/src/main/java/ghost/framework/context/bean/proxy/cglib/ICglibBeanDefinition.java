package ghost.framework.context.bean.proxy.cglib;
import ghost.framework.context.bean.IProxyBeanDefinition;
import ghost.framework.context.proxy.IProxyExecutor;
import net.sf.cglib.proxy.MethodInterceptor;
import java.util.Set;

/**
 * package: ghost.framework.context.bean.proxy.cglib
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:Cglib代绑定定义接口
 * @Date: 2020/2/9:12:10
 */
public interface ICglibBeanDefinition extends IProxyBeanDefinition, MethodInterceptor, IProxyExecutor {
    /**
     * 获取代理对象
     * @return
     */
    Object getProxyObject();
    /**
     * 获取代理执行器列表
     * @return
     */
    Set<IProxyExecutor> getExecutors();
}
