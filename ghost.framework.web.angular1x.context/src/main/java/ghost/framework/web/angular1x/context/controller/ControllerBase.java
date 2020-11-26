package ghost.framework.web.angular1x.context.controller;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.data.hibernate.ISessionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * package: ghost.framework.web.angular1x.admin.plugin.controller
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/3/13:15:17
 */
public abstract class ControllerBase {
    protected Log logger = LogFactory.getLog(this.getClass());
    /**
     * 注入会话工厂
     */
    @Autowired
    @Application
    protected ISessionFactory sessionFactory;
    protected void exception(Exception e){
        if (logger.isDebugEnabled()) {
            e.printStackTrace();
            logger.debug(e.getMessage(), e);
        } else {
            logger.error(e.getMessage(), e);
        }
    }
}
