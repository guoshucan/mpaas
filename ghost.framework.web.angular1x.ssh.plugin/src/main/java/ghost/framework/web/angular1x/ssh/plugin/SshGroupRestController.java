package ghost.framework.web.angular1x.ssh.plugin;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.data.hibernate.HibernateUtils;
import ghost.framework.util.IdGenerator;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.angular1x.ssh.plugin.entity.SshGroupEntity;
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

import javax.servlet.http.HttpServletResponse;
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
 * @Description:终端账号组管理控制器
 * @Date: 2020/5/25:11:22
 */
@RestController("/api")
public class SshGroupRestController extends ControllerBase {
    /**
     * 注入id生成接口
     */
    @Application
    @Autowired
    private IdGenerator generator;

    /**
     * 导出Excel
     *
     * @param ids      导出主键id列表，如果没有侧导出全部
     * @param response
     */
    @RequestMapping(value = "/4bf30eaf-ea8a-40c8-be5a-55c9d6c7e526")
    public void outExcel(@RequestBody RequestIds ids, HttpServletResponse response) {

    }

    /**
     * 添加管理组
     *
     * @param entity
     * @return
     */
    @RequestMapping(value = "/77a9f1ac-e836-449a-a8b6-4a565b52f4d4")
    public DataResponse add(@Valid @RequestBody SshGroupEntity entity) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.exists(session, SshGroupEntity.class, "name", entity.getName())) {
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
    @RequestMapping(value = "/cf97af68-9feb-4585-9631-7f83a60bbaf8")
    public DataResponse edit(@Valid @RequestBody SshGroupEntity body) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.notIdExists(session, SshGroupEntity.class, body.getId(), "name", body.getName())) {
                transaction.commit();
                dr.setError(1, "重复");
                return dr;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("name", body.getName());
            map.put("status", body.getStatus());
            map.put("description", body.getDescription());
            this.sessionFactory.update(session, SshGroupEntity.class, (Object) body.getId(), map);
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
    @RequestMapping(value = "/b7dab1c4-3e86-455b-ab11-a5c48cec4971")
    public DataResponse get(@RequestParam String id) {
        DataResponse dr = new DataResponse();
        try {
            SshGroupEntity groupEntity = this.sessionFactory.findById(SshGroupEntity.class, id);
            Map<String, Object> map = new HashMap<>();
            map.put("id", groupEntity.getId());
            map.put("name", groupEntity.getName());
            map.put("status", groupEntity.getStatus());
            map.put("description", groupEntity.getDescription());
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
     * @param id
     * @return
     */
    @RequestMapping(value = "/fa0633c2-f525-4945-b4f0-2608c78169ee")
    public DataResponse delete(@RequestParam String id) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            this.sessionFactory.deleteById(session, SshGroupEntity.class, id);
            this.sessionFactory.deleteById(session, SshServerEntity.class, "groupId", id);
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
    @RequestMapping(value = "/72089e92-54f1-470d-a9bd-a86cd463974d")
    public DataResponse statusUpdate(@RequestParam String id, @RequestParam int status) {
        DataResponse dr = new DataResponse();
        try {
            this.sessionFactory.update(SshGroupEntity.class, id, "status", status);
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
    @RequestMapping(value = "/e39cd079-1c65-4bb3-b0d9-fce15de6742b")
    public DataResponse batchDelete(@RequestBody RequestIds ids) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            this.sessionFactory.deleteByIds(session, SshGroupEntity.class, ids.getIds());
            this.sessionFactory.deleteByIds(session, SshServerEntity.class, "groupId", ids.getIds());
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
    @RequestMapping(value = "/5d110f4d-c366-4804-b675-f61fe45cb356")
    public SelectResponse select(@RequestBody SelectRequest request) {
        SelectResponse dr = new SelectResponse();
        try (Session session = this.sessionFactory.openSession()) {
            DetachedCriteria criteria = DetachedCriteria.forClass(SshGroupEntity.class);
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
                    new String[]{"id", "name", "status", "description", "createTime"});
            dr.setData(maps);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }
}