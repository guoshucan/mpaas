package ghost.framework.web.angular1x.module.manage.plugin.controller;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.data.hibernate.HibernateUtils;
import ghost.framework.util.IdGenerator;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.angular1x.module.manage.plugin.entitys.MavenRepositoryEntity;
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
import org.hibernate.criterion.*;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * package: ghost.framework.web.angular1x.module.manage.plugin.controller
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:maven仓库控制器
 * @Date: 2020/3/12:20:54
 */
@RestController("/api/module/management")
public class MavenRepositoryRestController extends ControllerBase {
    /**
     * 注入id生成接口
     */
    @Application
    @Autowired
    private IdGenerator generator;

    /**
     * 添加仓库组
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/250e4e9f-4cae-4986-a780-daf53223b5b6")
    public DataResponse add(@Valid @RequestBody MavenRepositoryEntity body) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.exists(session, MavenRepositoryEntity.class, "url", body.getUrl())) {
                transaction.commit();
                dr.setError(1, "重复");
                return dr;
            }
            MavenRepositoryEntity entity = new MavenRepositoryEntity();
            entity.setMavenRepositoryId(generator.generateId().toString());
            entity.setUrl(body.getUrl());
            entity.setStatus(body.getStatus());
            entity.setId(body.getId());
            entity.setDescription(body.getDescription());
            entity.setPassword(body.getPassword());
            entity.setUsername(body.getUsername());
            entity.setType(body.getType());
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
     * 修改仓库组信息
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/b2eb4c3f-4d44-4748-a2c3-cc05f308b584")
    public DataResponse edit(@Valid @RequestBody MavenRepositoryEntity body) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.notIdExists(session, MavenRepositoryEntity.class, body.getMavenRepositoryId(), "url", body.getUrl())) {
                transaction.commit();
                dr.setError(1, "重复");
                return dr;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("id", body.getId());
            map.put("type", body.getType());
            map.put("username", body.getUsername());
            map.put("password", body.getPassword());
            map.put("url", body.getUrl());
            map.put("description", body.getDescription());
            map.put("status", body.getStatus());
            this.sessionFactory.update(session, MavenRepositoryEntity.class, (Object) body.getMavenRepositoryId(), map);
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
     * 获取仓库组信息
     *
     * @param mavenRepositoryId
     * @return
     */
    @RequestMapping(value = "/e93e447f-506f-4b45-adb3-67b6436e0a2f")
    public DataResponse get(@RequestParam String mavenRepositoryId) {
        DataResponse dr = new DataResponse();
        try {
            MavenRepositoryEntity entity = this.sessionFactory.findById(MavenRepositoryEntity.class, mavenRepositoryId);
            Map<String, Object> map = new HashMap<>();
            map.put("mavenRepositoryId", entity.getMavenRepositoryId());
            map.put("id", entity.getId());
            map.put("type", entity.getType());
            map.put("username", entity.getUsername());
            map.put("password", entity.getPassword());
            map.put("url", entity.getUrl());
            map.put("description", entity.getDescription());
            map.put("status", entity.getStatus());
            dr.setData(map);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }

    /**
     * 删除仓库组
     *
     * @param mavenRepositoryId
     * @return
     */
    @RequestMapping(value = "/18fc1c23-80a9-4b68-96f4-fa4e0bd6d2c5")
    public DataResponse delete(@RequestParam String mavenRepositoryId) {
        DataResponse dr = new DataResponse();
        try {
            this.sessionFactory.deleteById(MavenRepositoryEntity.class, mavenRepositoryId);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }

    /**
     * 更新仓库组状态
     *
     * @param mavenRepositoryId
     * @param status
     * @return
     */
    @RequestMapping(value = "/f6c3c20a-6191-4b04-800a-e873a80390b7")
    public DataResponse statusUpdate(@RequestParam String mavenRepositoryId, @RequestParam int status) {
        DataResponse dr = new DataResponse();
        try {
            this.sessionFactory.update(MavenRepositoryEntity.class, mavenRepositoryId, "status", status);
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
    @RequestMapping(value = "/4d79a8e7-83cf-492e-96b9-8c2a9d06f4ea")
    public DataResponse batchDelete(@RequestBody RequestIds ids) {
        DataResponse dr = new DataResponse();
        try {
            this.sessionFactory.deleteByIds(MavenRepositoryEntity.class, "mavenRepositoryId", ids.getIds());
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
    @RequestMapping(value = "/431adf72-f0e0-4e79-b723-ade699967e3e")
    public SelectResponse select(@RequestBody SelectRequest request) {
        SelectResponse dr = new SelectResponse();
        try (Session session = this.sessionFactory.openSession()) {
            DetachedCriteria criteria = DetachedCriteria.forClass(MavenRepositoryEntity.class);
            if (!StringUtils.isEmpty(request.getKey())) {
                criteria.add(Restrictions.or(new SimpleExpression[]{
                        Restrictions.like("url", request.getKey(), MatchMode.ANYWHERE),
                        Restrictions.like("id", request.getKey(), MatchMode.ANYWHERE),
                        Restrictions.like("username", request.getKey(), MatchMode.ANYWHERE),
                        Restrictions.like("password", request.getKey(), MatchMode.ANYWHERE),
                        Restrictions.like("type", request.getKey(), MatchMode.ANYWHERE),
                        Restrictions.like("description", request.getKey(), MatchMode.ANYWHERE)
                }));
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
                    new String[]{"mavenRepositoryId", "id", "type", "username", "password", "url", "description", "status", "createTime"});
            dr.setData(maps);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }
}