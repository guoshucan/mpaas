package ghost.framework.web.angular1x.script.plugin;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.data.hibernate.HibernateUtils;
import ghost.framework.util.IdGenerator;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.angular1x.script.plugin.entity.CommandEntity;
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
 * package: ghost.framework.web.angular1x.ssh.script.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/8/25:22:42
 */
@RestController("/api")
public class CommandRestController extends ControllerBase {
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
    @RequestMapping(value = "/4504eed5-cf92-446d-913c-b4681fe0bd93")
    public DataResponse add(@Valid @RequestBody CommandEntity entity) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.exists(session, CommandEntity.class, "name", entity.getName())) {
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
     * @param id
     * @param status
     * @return
     */
    @RequestMapping(value = "/e75bf1f6-7a03-4657-85da-793e7626fbdf")
    public DataResponse statusUpdate(@RequestParam String id, @RequestParam short status) {
        DataResponse dr = new DataResponse();
        try {
            this.sessionFactory.update(CommandEntity.class, id, "status", status);
        } catch (Exception e) {
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
    @RequestMapping(value = "/7e9863de-c58d-48a4-a91b-ad7195541767")
    public DataResponse edit(@Valid @RequestBody CommandEntity body) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.notIdExists(session, CommandEntity.class, body.getId(), "name", body.getName())) {
                transaction.commit();
                dr.setError(1, "重复");
                return dr;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("name", body.getName());
            map.put("content", body.getContent());
            map.put("status", body.getStatus());
            map.put("description", body.getDescription());
            this.sessionFactory.update(session, CommandEntity.class, (Object) body.getId(), map);
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
    @RequestMapping(value = "/29fcb051-2180-48ba-8534-d8136f8a1427")
    public DataResponse get(@RequestParam String id) {
        DataResponse dr = new DataResponse();
        try {
            CommandEntity entity = this.sessionFactory.findById(CommandEntity.class, id);
            Map<String, Object> map = new HashMap<>();
            map.put("id", entity.getId());
            map.put("name", entity.getName());
            map.put("content", entity.getContent());
            map.put("status", entity.getStatus());
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
    @RequestMapping(value = "/0c41bdb7-cf2f-487b-8941-e4f6803b487c")
    public DataResponse delete(@RequestParam String id) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            this.sessionFactory.deleteById(session, CommandEntity.class, id);
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
     * 删除列表
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/408fe258-31a2-4ead-82b1-a9f140db9a73")
    public DataResponse batchDelete(@RequestBody RequestIds ids) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            this.sessionFactory.deleteByIds(session, CommandEntity.class, ids.getIds());
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

    @RequestMapping(value = "/208ecdb4-c312-42e5-8a08-f0a15459e63a")
    public DataResponse list() {
        DataResponse dr = new DataResponse();
        try {
            dr.setData(this.sessionFactory.list(CommandEntity.class));
        } catch (Exception e) {
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
    @RequestMapping(value = "/c35fd116-1f35-4f61-94cd-bcd1ba2843b4")
    public SelectResponse select(@RequestBody SelectRequest request) {
        SelectResponse dr = new SelectResponse();
        try (Session session = this.sessionFactory.openSession()) {
            DetachedCriteria criteria = DetachedCriteria.forClass(CommandEntity.class);
            if (!StringUtils.isEmpty(request.getKey())) {
                criteria.add(Restrictions.like("name", request.getKey(), MatchMode.ANYWHERE));
            }
            if (request.getStartTime() != null && request.getEndTime() == null) {
                criteria.add(Restrictions.ge("createTime", request.getStartTime()));
            }
            if (request.getStartTime() != null && request.getEndTime() != null) {
                criteria.add(Restrictions.between("createTime", request.getStartTime(), request.getEndTime()));
            }
            if (request.getStatus() != null && request.getStatus() != -1) {
                criteria.add(Restrictions.eq("status", request.getStatus().shortValue()));
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
                    new String[]{"id", "name", "status", "content", "createTime"});
            dr.setData(maps);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }
}
