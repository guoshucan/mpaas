package ghost.framework.web.angular1x.admin.plugin.controller;

import ghost.framework.admin.entity.AdminEntity;
import ghost.framework.admin.entity.AdminGroupEntity;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.data.hibernate.HibernateUtils;
import ghost.framework.util.IdGenerator;
import ghost.framework.web.angular1x.admin.plugin.controller.body.AdminGroupBody;
import ghost.framework.web.angular1x.admin.plugin.controller.body.AdminGroupEditBody;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.context.bind.annotation.RequestBody;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.bind.annotation.RequestParam;
import ghost.framework.web.context.bind.annotation.RestController;
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

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * package: ghost.framework.web.angular1x.admin.plugin.controller
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/3/12:20:54
 */
@RestController("/api")
public class AdminGroupRestController extends ControllerBase {
    /**
     * 注入id生成接口
     */
    @Application
    @Autowired
    private IdGenerator generator;

    /**
     * 添加管理组
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/6e6c11d4-6171-4f81-b485-5d8614b106b5")
    public DataResponse add(@Valid @RequestBody AdminGroupBody body) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.exists(session, AdminGroupEntity.class, "groupName", body.getGroupName())) {
                transaction.commit();
                dr.setError(1, "重复");
                return dr;
            }
            AdminGroupEntity entity = new AdminGroupEntity();
            entity.setGroupId(generator.generateId().toString());
            entity.setGroupName(body.getGroupName());
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
     * 修改管理组信息
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/1534cf96-90a4-4572-8a5b-d7fd873d3c92")
    public DataResponse edit(@Valid @RequestBody AdminGroupEditBody body) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.notIdExists(session, AdminGroupEntity.class, body.getGroupId(), "groupName", body.getGroupName())) {
                transaction.commit();
                dr.setError(1, "重复");
                return dr;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("groupName", body.getGroupName());
            map.put("status", body.getStatus());
            this.sessionFactory.update(session, AdminGroupEntity.class, (Object) body.getGroupId(), map);
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
     * 获取管理组信息
     *
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/1058740c-f97c-4d63-aea1-d3e099da21f1")
    public DataResponse get(@RequestParam String groupId) {
        DataResponse dr = new DataResponse();
        try {
            AdminGroupEntity groupEntity = this.sessionFactory.findById(AdminGroupEntity.class, groupId);
            Map<String, Object> map = new HashMap<>();
            map.put("groupId", groupEntity.getGroupId());
            map.put("groupName", groupEntity.getGroupName());
            map.put("status", groupEntity.getStatus());
            map.put("createTime", groupEntity.getCreateTime());
            dr.setData(map);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }

    /**
     * 删除管理组
     *
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/d595c6ea-9350-46e5-a0ac-f3372589acb7")
    public DataResponse delete(@RequestParam String groupId) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            this.sessionFactory.deleteById(session, AdminEntity.class, "groupId", groupId);
            this.sessionFactory.deleteById(session, AdminGroupEntity.class, groupId);
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
     * 更新管理组状态
     *
     * @param groupId
     * @param status
     * @return
     */
    @RequestMapping(value = "/6703b7ae-3ed3-4802-9fc2-2b5969bceb0f")
    public DataResponse statusUpdate(@RequestParam String groupId, @RequestParam int status) {
        DataResponse dr = new DataResponse();
        try {
            this.sessionFactory.update(AdminGroupEntity.class, groupId, "status", status);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }

    /**
     * 删除列表
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/33dc8b62-e702-48b7-9ed5-61c65f55943a")
    public DataResponse batchDelete(@RequestBody RequestIds ids) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //先删除管理员
            this.sessionFactory.deleteByIds(session, AdminEntity.class, "groupId", ids.getIds());
            //删除管理组
            this.sessionFactory.deleteByIds(session, AdminGroupEntity.class, ids.getIds());
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
     * 获取分页数据
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/0d10fb4e-d6a8-4914-a8a4-ccc13f7d0ec0")
    public SelectResponse select(@RequestBody SelectRequest request) {
        SelectResponse dr = new SelectResponse();
        try (Session session = this.sessionFactory.openSession()) {
            DetachedCriteria criteria = DetachedCriteria.forClass(AdminGroupEntity.class);
            if (!StringUtils.isEmpty(request.getKey())) {
                criteria.add(Restrictions.like("groupName", request.getKey(), MatchMode.ANYWHERE));
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
                //累加分页开始位置
                if (dr.getPages() - 1 == request.getStart()) {
                    request.setLength(new Long(dr.getCount() % request.getLength()).intValue());
                } else {
                    dr.setLength(request.getLength());
                }
            } else {
                dr.setLength(request.getLength());
            }
            //排序
            criteria.addOrder(Order.desc("createTime"));
            List<Map> maps = this.sessionFactory.mapSelect(
                    session,
                    criteria,
                    request.getStart(),
                    request.getLength(),
                    new String[]{"groupId", "groupName", "status", "createTime"});
            dr.setData(maps);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }
}