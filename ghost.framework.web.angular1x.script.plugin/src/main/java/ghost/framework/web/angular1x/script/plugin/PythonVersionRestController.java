package ghost.framework.web.angular1x.script.plugin;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.data.hibernate.HibernateUtils;
import ghost.framework.util.IdGenerator;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.angular1x.script.plugin.entity.PythonVersionEntity;
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
public class PythonVersionRestController extends ControllerBase {
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
    @RequestMapping(value = "/a2de3ae9-75a3-446a-b7c5-30b142037e04")
    public DataResponse add(@Valid @RequestBody PythonVersionEntity entity) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.exists(session, PythonVersionEntity.class, "name", entity.getName())) {
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
    @RequestMapping(value = "/a8a60988-fee7-4cf2-9a0c-b966d02a3ebb")
    public DataResponse edit(@Valid @RequestBody PythonVersionEntity body) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.notIdExists(session, PythonVersionEntity.class, body.getId(), "name", body.getName())) {
                transaction.commit();
                dr.setError(1, "重复");
                return dr;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("name", body.getName());
            this.sessionFactory.update(session, PythonVersionEntity.class, (Object) body.getId(), map);
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
    @RequestMapping(value = "/c782c191-38ac-4a9c-9ea9-67f1ef1a98b7")
    public DataResponse get(@RequestParam String id) {
        DataResponse dr = new DataResponse();
        try {
            PythonVersionEntity entity = this.sessionFactory.findById(PythonVersionEntity.class, id);
            Map<String, Object> map = new HashMap<>();
            map.put("id", entity.getId());
            map.put("name", entity.getName());
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
    @RequestMapping(value = "/2f15cb45-1920-4ac7-ba63-91355cf17022")
    public DataResponse delete(@RequestParam String id) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            this.sessionFactory.deleteById(session, PythonVersionEntity.class, id);
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
    @RequestMapping(value = "/c4dc7b71-2299-4585-9e43-99e766a91b78")
    public DataResponse batchDelete(@RequestBody RequestIds ids) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            this.sessionFactory.deleteByIds(session, PythonVersionEntity.class, ids.getIds());
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
    @RequestMapping(value = "/01bfbccb-7983-436c-880e-b8ec386236a6")
    public DataResponse list() {
        DataResponse dr = new DataResponse();
        try {
            dr.setData(this.sessionFactory.list(PythonVersionEntity.class));
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
    @RequestMapping(value = "/8c741271-78c0-4447-a8d0-a7bcc72bcd3c")
    public SelectResponse select(@RequestBody SelectRequest request) {
        SelectResponse dr = new SelectResponse();
        try (Session session = this.sessionFactory.openSession()) {
            DetachedCriteria criteria = DetachedCriteria.forClass(PythonVersionEntity.class);
            if (!StringUtils.isEmpty(request.getKey())) {
                criteria.add(Restrictions.like("name", request.getKey(), MatchMode.ANYWHERE));
            }
            if (request.getStartTime() != null && request.getEndTime() == null) {
                criteria.add(Restrictions.ge("createTime", request.getStartTime()));
            }
            if (request.getStartTime() != null && request.getEndTime() != null) {
                criteria.add(Restrictions.between("createTime", request.getStartTime(), request.getEndTime()));
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
                    new String[]{"id", "name", "createTime"});
            dr.setData(maps);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }
}