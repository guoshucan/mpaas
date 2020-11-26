package ghost.framework.web.angular1x.login.plugin.controller;
import ghost.framework.admin.entity.AdminEntity;
import ghost.framework.admin.entity.AdminGroupEntity;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.security.IPasswordDrive;
import ghost.framework.data.hibernate.HibernateUtils;
import ghost.framework.util.IdGenerator;
import ghost.framework.web.angular1x.admin.plugin.controller.body.AdminBody;
import ghost.framework.web.angular1x.admin.plugin.controller.body.AdminEditBody;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.context.bind.annotation.RequestBody;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.bind.annotation.RequestParam;
import ghost.framework.web.context.bind.annotation.RestController;
import ghost.framework.web.context.http.requestContent.EditPasswordRequest;
import ghost.framework.web.context.http.requestContent.RequestIds;
import ghost.framework.web.context.http.requestContent.SelectRequest;
import ghost.framework.web.context.http.responseContent.DataResponse;
import ghost.framework.web.context.http.responseContent.SelectResponse;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

/**
 * package: ghost.framework.web.angular1x.login.plugin.controller
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:管理员登录控制器
 * @Date: 2020/3/5:12:55
 */
@RestController("/api")
public class LoginRestController extends ControllerBase {
    /**
     * 注入密码驱动器接口
     */
    @Application
    @Autowired
    private IPasswordDrive passwordDrive;

    /**
     * 获取登录uri地址
     * @return
     */
    @RequestMapping(value = "/ca2d33ea-6384-449d-a28d-991e048bad99")
    public String getLoginUri() {
        return "access.b03a82b1-a197-48a0-a6d1-4d2e1a996fc4";
    }

//    /**
//     * 获取退出登录uri地址
//     * @return
//     */
//    @RequestMapping(value = "/a5618679-bb8a-4963-bae7-1cb1fd31d2ed")
//    public String getOutLoginUri() {
//        return "";
//    }
    /**
     * 登录
     *
     * @param info
     * @return
     */
    @RequestMapping("/d73a3f84-048b-49fc-85aa-e2c6c1be4902")
    public DataResponse login(SignupInfo info) {
        DataResponse response = new DataResponse();

        return response;
    }
    /**
     * 注入id生成接口
     */
    @Application
    @Autowired
    private IdGenerator generator;
    /**
     *  用户注册接口
     * @param info
     * @return
     */
    @RequestMapping("/4949f374-74c0-4dc4-b0df-1eb10564d86d")
    public DataResponse signup(@RequestBody SignupInfo info) {
        DataResponse response = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //获取默认管理员组
            String groupId = this.sessionFactory.uniqueResult(session, AdminGroupEntity.class, "def", true, "groupId");
            if (groupId == null) {
                transaction.rollback();
                response.setError(1, "默认管理组不存在！");
                return response;
            }
            AdminEntity entity = new AdminEntity();
            //判断是否有管理员存在
            if (HibernateUtils.exists(session, AdminEntity.class, new String[]{"mobilePhone"}, new Object[]{info.getMobilePhone()})) {
                transaction.rollback();
                response.setError(2, "账户已经存在！");
                return response;
            }
            entity.setAdminId(this.generator.generateId().toString());
            entity.setGroupId(groupId);
            entity.setAdminName(info.getAdminName());
            entity.setMobilePhone(info.getMobilePhone());
            session.save(entity);
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
     * @param info
     * @return
     */
    @RequestMapping("/1320b562-1582-42f7-90ae-7ad8a4b05440")
    public DataResponse initAdmin(SignupInfo info) {
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
     * 是否登录
     *
     * @param session
     * @return
     */
    @RequestMapping("/0eda3ff1-6c6c-48a0-adb8-de34a7debf75")
    public DataResponse isLogin(HttpSession session) {
        DataResponse response = new DataResponse();
        response.setData(true);
        return response;
    }

    /**
     * 退出登录
     *
     * @return
     */
    @RequestMapping("/fca58346-8e17-498d-9902-e90748ca890a")
    public DataResponse outLogin() {
        DataResponse response = new DataResponse();

        return response;
    }
    /**
     * 添加管理员接口
     * @param body
     * @return
     */
    @RequestMapping(value = "/bc9815c5-0d88-470c-8bb3-5386004966ba")
    public DataResponse add(@Valid @RequestBody AdminBody body) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.exists(session, AdminEntity.class, "adminUser", body.getAdminUser())) {
                transaction.commit();
                dr.setError(1, "重复");
                return dr;
            }
            AdminEntity entity = new AdminEntity();
//            entity.setAdminId(generator.generateId().toString());
            entity.setAdminName(body.getAdminName());
            entity.setGroupId(body.getGroupId());
            entity.setAddress(body.getAddress());
            entity.setEmail(body.getEmail());
            entity.setWeixin(body.getWeixin());
            entity.setQq(body.getQq());
            entity.setPassword(this.passwordDrive.sha(body.getPassword()));
            entity.setMobilePhone(body.getMobilePhone());
            entity.setAdminUser(body.getAdminUser());
            entity.setStatus(body.getStatus());
            entity.setCreateTime(new Date());
            session.save(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }

    /**
     * 修改管理员
     * @param body
     * @return
     */
    @RequestMapping(value = "/21bdb51a-24dc-44a1-9fc4-e05cb8ec4971")
    public DataResponse edit(@Valid @RequestBody AdminEditBody body) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.notIdExists(session, AdminEntity.class, body.getAdminId(), "adminUser", body.getAdminUser())) {
                transaction.rollback();
                dr.setError(1, "重复");
                return dr;
            }
            Map<String, Object> where = new HashMap<>();
            where.put("adminId", body.getAdminId());
            Map<String, Object> map = new HashMap<>();
            map.put("groupId", body.getGroupId());
            map.put("adminName", body.getAdminName());
            map.put("address", body.getAddress());
            map.put("email", body.getEmail());
            map.put("mobilePhone", body.getMobilePhone());
            map.put("qq", body.getQq());
            map.put("weixin", body.getWeixin());
            map.put("status", body.getStatus());
            this.sessionFactory.update(session, AdminEntity.class, (Object) body.getAdminId(), map);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }

    /**
     * 更新管理员状态
     * @param adminId 管理员id
     * @param status 管理员状态
     * @return
     */
    @RequestMapping(value = "/8cfd1bb7-22de-46b3-b48b-729118ea34bc")
    public DataResponse statusUpdate(@RequestParam String adminId, @RequestParam int status) {
        DataResponse dr = new DataResponse();
        try {
            this.sessionFactory.update(AdminEntity.class, adminId, "status", status);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }

    /**
     * 删除管理员信息
     * @param adminId 管理员id
     * @return
     */
    @RequestMapping(value = "/3d5d6e6d-4d59-426e-ab25-955825387c0a")
    public DataResponse delete(@RequestParam String adminId) {
        DataResponse dr = new DataResponse();
        try {
            this.sessionFactory.deleteById(AdminEntity.class, adminId);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }

    /**
     * 重置密码
     * @param adminId
     * @return
     */
    @RequestMapping(value = "/8f928d25-101d-431a-b1d0-47b2059c63e1")
    public DataResponse resetPassword(@RequestParam String adminId) {
        DataResponse dr = new DataResponse();
        try {
            String pass = UUID.randomUUID().toString().substring(0, 8);
            dr.setData(pass);
            pass = this.passwordDrive.sha(pass);
            this.sessionFactory.update(AdminEntity.class, adminId, "password", pass);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }
    /**
     * 获取管理员信息
     * @param adminId 管理员id
     * @return
     */
    @RequestMapping(value = "/121bf996-cd8c-432f-a4d6-787c0069d170")
    public DataResponse get(@RequestParam String adminId) {
        DataResponse dr = new DataResponse();
        try {
            dr.setData(this.sessionFactory.map(DetachedCriteria.forClass(AdminEntity.class).createAlias("groupEntity", "g").add(Restrictions.eq("adminId", adminId)),
                    new String[]{"adminId", "groupId", "g.groupName", "adminName", "adminUser", "createTime", "address", "email", "mobilePhone", "qq", "weixin", "status"}));
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }

    /**
     * 批量删除管理员
     * @param ids 管理员id列表
     * @return
     */
    @RequestMapping(value = "/fa793855-c8ad-465f-baba-646c2c225eb7")
    public DataResponse batchDelete(@RequestBody RequestIds ids) {
        DataResponse dr = new DataResponse();
        try {
            this.sessionFactory.deleteByIds(AdminEntity.class, ids.getIds());
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }

    public static class GroupSelectRequest extends SelectRequest {

        private String groupId;

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }
    }
    @RequestMapping(value = "/6e5fee72-3800-4c23-b95a-c8797c9f6e6c")
    public DataResponse editPassword(@RequestBody EditPasswordRequest request){
        DataResponse dr = new DataResponse();
        try {
            request.setPassword(this.passwordDrive.sha(request.getPassword()));
            this.sessionFactory.update(AdminEntity.class, request.getId(), "password", request.getPassword());
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }
    /**
     * 获取管理员列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/527ac642-3df8-4844-85db-bae9ae6c271a")
    public SelectResponse select(@RequestBody GroupSelectRequest request) {
        SelectResponse dr = new SelectResponse();
        try (Session session = this.sessionFactory.openSession()) {
            DetachedCriteria criteria = DetachedCriteria.forClass(AdminEntity.class);//.createAlias("groupEntity", "g");
            if (!StringUtils.isEmpty(request.getKey())) {
                criteria.add(Restrictions.like("adminName", request.getKey(), MatchMode.ANYWHERE));
            }
            if (!StringUtils.isEmpty(request.getGroupId())) {
                criteria.add(Restrictions.eq("groupId", request.getGroupId()));
            }
            if (request.getStartTime() != null && request.getEndTime() == null) {
                criteria.add(Restrictions.ge("createTime", request.getStartTime()));
            }
            if (request.getStartTime() != null && request.getEndTime() != null) {
                criteria.add(Restrictions.between("createTime", request.getStartTime(), request.getEndTime()));
            }
            if (request.getStatus() != -1) {
                criteria.add(Restrictions.eq("status", new Integer(request.getStatus()).shortValue()));
            }
            dr.setCount(this.sessionFactory.count(session, criteria));
            dr.setPages(dr.getCount() / request.getLength());
            request.setStart(request.getStart() * request.getLength());
            if ((dr.getCount() % request.getLength()) != 0) {
                dr.setPages(dr.getPages() + 1);
                if (dr.getPages() - 1 == request.getStart()) {
                    dr.setLength(new Long(dr.getCount() % request.getLength()).intValue());
                } else {
                    dr.setLength(request.getLength());
                }
            } else {
                dr.setLength(request.getLength());
            }
            criteria = criteria.createAlias("groupEntity", "g");
            //排序
            criteria.addOrder(Order.desc("createTime"));
            List<Map> maps = this.sessionFactory.mapSelect(
                    session,
                    criteria,
                    request.getStart(),
                    request.getLength(),
                    new String[]{"adminId", "adminName", "adminUser", "groupId", "g.groupName", "status", "createTime"});
            dr.setData(maps);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }
}