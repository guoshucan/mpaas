package ghost.framework.web.angular1x.ssh.plugin;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.data.hibernate.HibernateUtils;
import ghost.framework.ssh.SshUtil;
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
import java.util.*;
/**
 * package: ghost.framework.web.angular1x.ssh.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:终端账号管理控制器
 * @Date: 2020/5/25:11:21
 */
@RestController("/api")
public class SshServerRestController extends ControllerBase {
    /**
     * 注入id生成接口
     */
    @Application
    @Autowired
    private IdGenerator generator;

    /**
     * 添加管理员接口
     *
     * @param entity
     * @return
     */
    @RequestMapping(value = "/6e8e98ea-1bac-4d7e-892e-a15cdfd3afb3")
    public DataResponse add(@Valid @RequestBody SshServerEntity entity) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.exists(session, SshServerEntity.class, "name", entity.getName())) {
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
     * 修改管理员
     *
     * @param entity
     * @return
     */
    @RequestMapping(value = "/02c7feab-99a8-4b35-84d0-f7b7c64bc488")
    public DataResponse edit(@Valid @RequestBody SshServerEntity entity) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.notIdExists(session, SshServerEntity.class, entity.getId(), "name", entity.getName())) {
                transaction.rollback();
                dr.setError(1, "重复");
                return dr;
            }
            Map<String, Object> where = new HashMap<>();
            where.put("id", entity.getId());
            Map<String, Object> map = new HashMap<>();
            map.put("groupId", entity.getGroupId());
            map.put("regionId", entity.getRegionId());
            map.put("name", entity.getName());
            map.put("hostName", entity.getHostName());
            map.put("timeout", entity.getTimeout());
            map.put("channelTimeout", entity.getChannelTimeout());
            map.put("typeId", entity.getTypeId());
            map.put("remoteDirectory", entity.getRemoteDirectory());
            map.put("status", entity.getStatus());
            map.put("accountId", entity.getAccountId());
            map.put("description", entity.getDescription());
            this.sessionFactory.update(session, SshServerEntity.class, (Object) entity.getId(), map);
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
     *
     * @param id     管理员id
     * @param status 管理员状态
     * @return
     */
    @RequestMapping(value = "/fdc3811b-4504-46b1-8a61-b6df31b53b37")
    public DataResponse statusUpdate(@RequestParam String id, @RequestParam int status) {
        DataResponse dr = new DataResponse();
        try {
            this.sessionFactory.update(SshServerEntity.class, id, "status", status);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }

    /**
     * 删除管理员信息
     *
     * @param id 管理员id
     * @return
     */
    @RequestMapping(value = "/b2062b6e-d4bb-4ac7-9834-a427e0a30aa9")
    public DataResponse delete(@RequestParam String id) {
        DataResponse dr = new DataResponse();
        try {
            this.sessionFactory.deleteById(SshServerEntity.class, id);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }
    /**
     * 获取管理员信息
     *
     * @param id 管理员id
     * @return
     */
    @RequestMapping(value = "/34d19d4a-bb85-4340-bf39-b9065c6ed934")
    public DataResponse get(@RequestParam String id) {
        DataResponse dr = new DataResponse();
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(SshServerEntity.class);
            criteria.createAlias("groupEntity", "g");
            criteria.createAlias("regionEntity", "r");
            criteria.createAlias("typeEntity", "t");
            criteria.createAlias("accountEntity", "a");
            criteria.add(Restrictions.eq("id", id));
            dr.setData(this.sessionFactory.map(criteria,
                    new String[]{"id", "name",
                            "a.name as accountName", "a.id as accountId",
                            "g.id as groupId", "g.name as groupName",
                            "r.id as regionId", "r.name as regionName",
                            "t.id as typeId", "t.name as typeName", "t.version as version",
                            "hostName", "port", "status", "timeout", "channelTimeout", "remoteDirectory", "description", "createTime"}));
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }
    /**
     * 批量删除管理员
     *
     * @param ids 管理员id列表
     * @return
     */
    @RequestMapping(value = "/aed4d3d1-a0a3-4286-99cf-debf6a77f449")
    public DataResponse batchDelete(@RequestBody RequestIds ids) {
        DataResponse dr = new DataResponse();
        try {
            this.sessionFactory.deleteByIds(SshServerEntity.class, ids.getIds());
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }

    public static class SelectRequestEx extends SelectRequest {

        private String groupId;

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        private String regionId;

        public String getRegionId() {
            return regionId;
        }

        public void setRegionId(String regionId) {
            this.regionId = regionId;
        }

        public String getTypeId() {
            return typeId;
        }

        private String typeId;

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        private String accountId;
    }
    /**
     * 测试连接ssh服务器
     *
     * @param id ssh服务器id
     * @return
     */
    @RequestMapping(value = "/3fa73f23-2d27-426b-aabe-4901451225ba")
    public DataResponse testConnection(@RequestParam String id) {
        DataResponse dr = new DataResponse();
        try (Session session = this.sessionFactory.openSession()) {
            //获取ssh服务器
            SshServerEntity entity = this.sessionFactory.findById(session, SshServerEntity.class, id);
            SshAccountEntity accountEntity = entity.getAccountEntity();
            if (entity == null) {
                dr.setError(-2, "null");
            }
            //测试连接
            dr.setData(SshUtil.ganymedTestConnection(entity.getHostName(), entity.getPort(), accountEntity.getName(), accountEntity.getPassword()));
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }

    /**
     * 获取管理员列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/e3fd026c-685b-475f-9ddd-ec58a20441cc")
    public SelectResponse select(@RequestBody SelectRequestEx request) {
        SelectResponse dr = new SelectResponse();
        try (Session session = this.sessionFactory.openSession()) {
            DetachedCriteria criteria = DetachedCriteria.forClass(SshServerEntity.class);
            criteria.createAlias("regionEntity", "r");
            criteria.createAlias("typeEntity", "t");
            criteria.createAlias("accountEntity", "a");
            criteria.createAlias("groupEntity", "g");
            if (!StringUtils.isEmpty(request.getKey())) {
                criteria.add(Restrictions.like("name", request.getKey(), MatchMode.ANYWHERE));
            }
            if (!StringUtils.isEmpty(request.getGroupId())) {
                criteria.add(Restrictions.eq("g.id", request.getGroupId()));
            }
            if (!StringUtils.isEmpty(request.getRegionId())) {
                criteria.add(Restrictions.eq("r.id", request.getRegionId()));
            }
            if (!StringUtils.isEmpty(request.getTypeId())) {
                criteria.add(Restrictions.eq("t.id", request.getTypeId()));
            }
            if (!StringUtils.isEmpty(request.getAccountId())) {
                criteria.add(Restrictions.eq("a.id", request.getAccountId()));
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
            //排序
            criteria.addOrder(Order.desc("createTime"));
            List<Map> maps = this.sessionFactory.mapSelect(
                    session,
                    criteria,
                    request.getStart(),
                    request.getLength(),
                    new String[]{"id", "name",
                            "a.name as accountName", "a.id as accountId",
                            "g.id as groupId", "g.name as groupName",
                            "r.id as regionId", "r.name as regionName",
                            "t.id as typeId", "t.name as typeName", "t.version as version",
                            "hostName", "port", "status", "timeout", "channelTimeout","createTime"});
            dr.setData(maps);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }
}
