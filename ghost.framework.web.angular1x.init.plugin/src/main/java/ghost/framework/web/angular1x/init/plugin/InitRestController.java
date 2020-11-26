package ghost.framework.web.angular1x.init.plugin;
import ghost.framework.admin.entity.AdminEntity;
import ghost.framework.admin.entity.AdminGroupEntity;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.security.IPasswordDrive;
import ghost.framework.data.hibernate.HibernateUtils;
import ghost.framework.util.IdGenerator;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.bind.annotation.RestController;
import ghost.framework.web.context.http.responseContent.DataResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;
/**
 * package: ghost.framework.web.angular1x.init.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:初始化控制器
 * @Date: 2020/3/5:12:55
 */
@RestController("/api")
public class InitRestController extends ControllerBase {
    /**
     * 注入密码驱动器接口
     */
    @Application
    @Autowired
    private IPasswordDrive passwordDrive;
    /**
     * 注入id生成接口
     */
    @Application
    @Autowired
    private IdGenerator generator;

    @RequestMapping("/a8370638-ae7f-49e5-81f5-1197f1fd5323")
    public DataResponse isInitDatabase() {
        DataResponse response = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否有管理员存在
            if (HibernateUtils.exists(session, AdminGroupEntity.class) || HibernateUtils.exists(session, AdminEntity.class)) {
                transaction.rollback();
                response.setError(1, "已经存在管理员！");
                return response;
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            this.exception(e);
            response.setError(-1, e.getMessage());
        }
        return response;
    }
    @RequestMapping("/e13d69f6-35c9-4187-861c-a1967c5e4f9c")
    public DataResponse isInitResourceContainer(){
        DataResponse response = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否有管理员存在
            if (HibernateUtils.exists(session, AdminGroupEntity.class) || HibernateUtils.exists(session, AdminEntity.class)) {
                transaction.rollback();
                response.setError(1, "已经存在管理员！");
                return response;
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            this.exception(e);
            response.setError(-1, e.getMessage());
        }
        return response;
    }
    /**
     * 是否初始化管理员
     *
     * @return
     */
    @RequestMapping("/b017f5ea-2dfd-4af4-8a99-4dca428a6714")
    public DataResponse isInitAdmin() {
        DataResponse response = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否有管理员存在
            if (HibernateUtils.exists(session, AdminGroupEntity.class) || HibernateUtils.exists(session, AdminEntity.class)) {
                transaction.rollback();
                response.setError(1, "已经存在管理员！");
                return response;
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            this.exception(e);
            response.setError(-1, e.getMessage());
        }
        return response;
    }

    /**
     * 初始化管理员
     *
     * @param info
     * @return
     */
    @RequestMapping("/1320b562-1582-42f7-90ae-7ad8a4b05440")
    public DataResponse initAdmin(InitInfo info) {
        DataResponse response = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否有管理员存在
            if (HibernateUtils.exists(session, AdminGroupEntity.class) || HibernateUtils.exists(session, AdminEntity.class)) {
                transaction.rollback();
                response.setError(1, "已经存在管理员！");
                return response;
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            this.exception(e);
            response.setError(-1, e.getMessage());
        }
        return response;
    }
}