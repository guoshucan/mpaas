package ghost.framework.web.angular1x.ssh.plugin;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.data.hibernate.HibernateUtils;
import ghost.framework.util.IdGenerator;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.angular1x.ssh.plugin.entity.SshAccountEntity;
import ghost.framework.web.angular1x.ssh.plugin.entity.SshServerEntity;
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
 * package: ghost.framework.web.angular1x.ssh.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/8/22:18:06
 */
@RestController("/api")
public class SshAccountRestController extends ControllerBase {
    /**
     * 注入id生成接口
     */
    @Application
    @Autowired
    private IdGenerator generator;
    /**
     * 添加管理组
     *
     * @param entity
     * @return
     */
    @RequestMapping(value = "/23ca2b7b-80ec-4153-88b6-9b0a4c3b784f")
    public DataResponse add(@Valid @RequestBody SshAccountEntity entity) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.exists(session, SshAccountEntity.class, "name", entity.getName())) {
                transaction.commit();
                dr.setError(1, "重复");
                return dr;
            }
            entity.setId(generator.generateId().toString());
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
    @RequestMapping(value = "/d49237c3-70d4-46c1-899e-bb883197d40e")
    public DataResponse edit(@Valid @RequestBody SshAccountEntity body) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.notIdExists(session, SshAccountEntity.class, body.getId(), "name", body.getName())) {
                transaction.commit();
                dr.setError(1, "重复");
                return dr;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("name", body.getName());
            map.put("status", body.getStatus());
            map.put("root", body.isRoot());
            map.put("password", body.getPassword());
            map.put("description", body.getDescription());
            this.sessionFactory.update(session, SshAccountEntity.class, (Object) body.getId(), map);
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
     * @param id
     * @return
     */
    @RequestMapping(value = "/fd0d4fb1-0653-473e-ba62-096404cb01d9")
    public DataResponse get(@RequestParam String id) {
        DataResponse dr = new DataResponse();
        try {
            SshAccountEntity entity = this.sessionFactory.findById(SshAccountEntity.class, id);
            Map<String, Object> map = new HashMap<>();
            map.put("id", entity.getId());
            map.put("name", entity.getName());
            map.put("root", entity.isRoot());
            map.put("status", entity.getStatus());
            map.put("password", entity.getPassword());
            map.put("description", entity.getDescription());
            map.put("createTime", entity.getCreateTime());
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
     * @param id
     * @return
     */
    @RequestMapping(value = "/c6afe6a9-6175-4374-86f8-6d745aefa56a")
    public DataResponse delete(@RequestParam String id) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            this.sessionFactory.deleteById(session, SshAccountEntity.class, id);
            this.sessionFactory.deleteById(session, SshServerEntity.class, "accountId", id);
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
     * @param id
     * @param status
     * @return
     */
    @RequestMapping(value = "/1740c9cb-c5ff-4108-bcf3-50ff037eb93c")
    public DataResponse statusUpdate(@RequestParam String id, @RequestParam int status) {
        DataResponse dr = new DataResponse();
        try {
            this.sessionFactory.update(SshAccountEntity.class, id, "status", status);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }

    @RequestMapping(value = "/bd5db608-9521-4d28-bd9e-e84aa5307496")
    public DataResponse rootUpdate(@RequestParam String id, @RequestParam boolean root) {
        DataResponse dr = new DataResponse();
        try {
            this.sessionFactory.update(SshAccountEntity.class, id, "root", root);
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
    @RequestMapping(value = "/aa0d7b48-0032-4602-9aa1-799c5d0deb1a")
    public DataResponse batchDelete(@RequestBody RequestIds ids) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            this.sessionFactory.deleteByIds(session, SshAccountEntity.class, ids.getIds());
            this.sessionFactory.deleteByIds(session, SshServerEntity.class, "accountId", ids.getIds());
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
    public static class RootSelectRequest extends SelectRequest{
        private Boolean root;

        public Boolean getRoot() {
            return root;
        }

        public void setRoot(Boolean root) {
            this.root = root;
        }
    }
    /**
     * 获取分页数据
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/aa1d3d07-0382-4288-ad27-055a24b2f5cf")
    public SelectResponse select(@RequestBody RootSelectRequest request) {
        SelectResponse dr = new SelectResponse();
        try (Session session = this.sessionFactory.openSession()) {
            DetachedCriteria criteria = DetachedCriteria.forClass(SshAccountEntity.class);
            if (!StringUtils.isEmpty(request.getKey())) {
                criteria.add(Restrictions.like("name", request.getKey(), MatchMode.ANYWHERE));
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
            if (request.getRoot() != null) {
                criteria.add(Restrictions.eq("root", request.getRoot().booleanValue()));
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
                    new String[]{"id", "name", "status", "root", "description", "createTime"});
            dr.setData(maps);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }
}