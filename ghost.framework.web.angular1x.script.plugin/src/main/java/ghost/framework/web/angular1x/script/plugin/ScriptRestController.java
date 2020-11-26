package ghost.framework.web.angular1x.script.plugin;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.data.hibernate.HibernateUtils;
import ghost.framework.util.IdGenerator;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.angular1x.script.plugin.entity.GoLangVersionEntity;
import ghost.framework.web.angular1x.script.plugin.entity.JavaVersionEntity;
import ghost.framework.web.angular1x.script.plugin.entity.PythonVersionEntity;
import ghost.framework.web.angular1x.script.plugin.entity.ScriptEntity;
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
import org.hibernate.sql.JoinType;

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
 * @Date: 2020/8/25:10:12
 */
@RestController("/api")
public class ScriptRestController extends ControllerBase {
    /**
     * 注入id生成接口
     */
    @Application
    @Autowired
    private IdGenerator generator;

    /**
     * @param entity
     * @return
     */
    @RequestMapping(value = "/50f247b3-1530-4bb7-a660-7c171006d2b7")
    public DataResponse add(@Valid @RequestBody ScriptEntity entity) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.exists(session, ScriptEntity.class, "name", entity.getName())) {
                transaction.commit();
                dr.setError(1, "重复");
                return dr;
            }
            if (StringUtils.isEmpty(entity.getPythonVersionId())) {
                entity.setPythonVersionId(null);
            }
            if (StringUtils.isEmpty(entity.getGolangVersionId())) {
                entity.setGolangVersionId(null);
            }
            if (StringUtils.isEmpty(entity.getJavaVersionId())) {
                entity.setJavaVersionId(null);
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
     * @param entity
     * @return
     */
    @RequestMapping(value = "/99b6ab8f-715b-4dc4-a0dc-8741135c5fc4")
    public DataResponse edit(@Valid @RequestBody ScriptEntity entity) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.notIdExists(session, ScriptEntity.class, entity.getId(), "name", entity.getName())) {
                transaction.commit();
                dr.setError(1, "重复");
                return dr;
            }
            if (StringUtils.isEmpty(entity.getPythonVersionId())) {
                entity.setPythonVersionId(null);
            }
            if (StringUtils.isEmpty(entity.getGolangVersionId())) {
                entity.setGolangVersionId(null);
            }
            if (StringUtils.isEmpty(entity.getJavaVersionId())) {
                entity.setJavaVersionId(null);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("name", entity.getName());
            map.put("status", entity.getStatus());
            map.put("type", entity.getType());
            map.put("pythonVersionId", entity.getPythonVersionId());
            map.put("golangVersionId", entity.getGolangVersionId());
            map.put("javaVersionId", entity.getJavaVersionId());
            map.put("content", entity.getContent());
            map.put("description", entity.getDescription());
            this.sessionFactory.update(session, ScriptEntity.class, (Object) entity.getId(), map);
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
     * @return
     */
    @RequestMapping(value = "/41610841-35c3-4ee0-afe8-682eb9e5856c")
    public DataResponse get(@RequestParam String id) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            ScriptEntity entity = this.sessionFactory.findById(session, ScriptEntity.class, id);
            PythonVersionEntity pythonVersionEntity = null;
            if (!ghost.framework.util.StringUtils.isEmpty(entity.getPythonVersionId())) {
                pythonVersionEntity = entity.getPythonVersionEntity();
            }
            GoLangVersionEntity goLangVersionEntity = null;
            if (!ghost.framework.util.StringUtils.isEmpty(entity.getGolangVersionId())) {
                goLangVersionEntity = entity.getGoLangVersionEntity();
            }
            JavaVersionEntity javaVersionEntity = entity.getJavaVersionEntity();
            if (!ghost.framework.util.StringUtils.isEmpty(entity.getJavaVersionId())) {
                javaVersionEntity = entity.getJavaVersionEntity();
            }
            transaction.commit();
            Map<String, Object> map = new HashMap<>();
            map.put("id", entity.getId());
            map.put("name", entity.getName());
            map.put("status", entity.getStatus());
            map.put("type", entity.getType());
            if (pythonVersionEntity == null) {
                map.put("pythonVersionId", null);
                map.put("pythonVersion", null);
            } else {
                map.put("pythonVersionId", pythonVersionEntity.getId());
                map.put("pythonVersion", pythonVersionEntity.getName());
            }
            if (goLangVersionEntity == null) {
                map.put("golangVersionId", null);
                map.put("golangVersion", null);
            } else {
                map.put("golangVersionId", goLangVersionEntity.getId());
                map.put("golangVersion", goLangVersionEntity.getName());
            }
            if (javaVersionEntity == null) {
                map.put("javaVersionId", null);
                map.put("javaVersion", null);
            } else {
                map.put("javaVersionId", javaVersionEntity.getId());
                map.put("javaVersion", javaVersionEntity.getName());
            }
            map.put("content", entity.getContent());
            map.put("description", entity.getDescription());
            map.put("createTime", entity.getCreateTime());
            dr.setData(map);
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
     * @return
     */
    @RequestMapping(value = "/f3c5ee68-3c0c-43b6-9b0a-10489b04b260")
    public DataResponse delete(@RequestParam String id) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            this.sessionFactory.deleteById(session, ScriptEntity.class, id);
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
    @RequestMapping(value = "/1c78f9bc-bbf8-46d9-a9df-1e9320ed5572")
    public DataResponse statusUpdate(@RequestParam String id, @RequestParam short status) {
        DataResponse dr = new DataResponse();
        try {
            this.sessionFactory.update(ScriptEntity.class, id, "status", status);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }

    /**
     * @param ids
     * @return
     */
    @RequestMapping(value = "/fb9c9e77-6107-44ba-ac0a-01ef1ff72ed9")
    public DataResponse batchDelete(@RequestBody RequestIds ids) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            this.sessionFactory.deleteByIds(session, ScriptEntity.class, ids.getIds());
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

    public static class ExSelectRequest extends SelectRequest {
        private String golangVersionId;

        public String getGolangVersionId() {
            return golangVersionId;
        }

        public void setGolangVersionId(String golangVersionId) {
            this.golangVersionId = golangVersionId;
        }

        public void setPythonVersionId(String pythonVersionId) {
            this.pythonVersionId = pythonVersionId;
        }

        public String getPythonVersionId() {
            return pythonVersionId;
        }

        private String pythonVersionId;
        private String javaVersionId;

        public void setJavaVersionId(String javaVersionId) {
            this.javaVersionId = javaVersionId;
        }

        public String getJavaVersionId() {
            return javaVersionId;
        }

        private Short type;

        public Short getType() {
            return type;
        }

        public void setType(Short type) {
            this.type = type;
        }
    }

    /**
     * 获取分页数据
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/1c25c1c8-e315-4dea-892e-73f9aedf984f")
    public SelectResponse select(@RequestBody ExSelectRequest request) {
        SelectResponse dr = new SelectResponse();
        try (Session session = this.sessionFactory.openSession()) {
            DetachedCriteria criteria = DetachedCriteria.forClass(ScriptEntity.class);
            criteria.createAlias("pythonVersionEntity", "p", JoinType.LEFT_OUTER_JOIN);
            criteria.createAlias("goLangVersionEntity", "g", JoinType.LEFT_OUTER_JOIN);
            criteria.createAlias("javaVersionEntity", "j", JoinType.LEFT_OUTER_JOIN);
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
            if (request.getType() != null && request.getType() >= 0) {
                criteria.add(Restrictions.eq("type", request.getType().shortValue()));
                if (!StringUtils.isEmpty(request.getGolangVersionId())) {
                    criteria.add(Restrictions.eq("golangVersionId", request.getGolangVersionId()));
                }
                if (!StringUtils.isEmpty(request.getPythonVersionId())) {
                    criteria.add(Restrictions.eq("pythonVersionId", request.getPythonVersionId()));
                }
                if (!StringUtils.isEmpty(request.getJavaVersionId())) {
                    criteria.add(Restrictions.eq("javaVersionId", request.getJavaVersionId()));
                }
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
                    new String[]{"id", "name", "status", "type",
                            "p.name as pythonVersion", "pythonVersionId",
                            "g.name as golangVersion", "golangVersionId",
                            "j.name as javaVersion", "javaVersionId",
                            "description", "createTime"});
            dr.setData(maps);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }
}