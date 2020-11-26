package ghost.framework.web.angular1x.resource.container.manage.plugin;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.data.hibernate.HibernateUtils;
import ghost.framework.resource.container.entity.*;
import ghost.framework.util.IdGenerator;
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
 * package: ghost.framework.web.angular1x.resource.container.manage.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:资源容器管理控制器
 * @Date: 2020/3/12:20:54
 */
@RestController("/api")
public class ResourceContainerManageRestController extends ControllerBase {
    /**
     * 注入id生成接口
     */
    @Application
    @Autowired
    private IdGenerator generator;
    /**
     * 修改Minio
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/dc20bea9-64c4-47cb-adda-c741f3c251d8")
    public DataResponse editMinio(@Valid @RequestBody MinioBody body) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.notIdExists(session, ResourceContainerEntity.class, body.getId(), "name", body.getName())) {
                transaction.commit();
                dr.setError(1, "重复");
                return dr;
            }
            //更新主表
            Map<String, Object> map = new HashMap<>();
            map.put("name", body.getName());
            map.put("status", body.getStatus());
            map.put("endpoint", body.getEndpoint());
            map.put("type", body.getType());
            this.sessionFactory.update(session, ResourceContainerEntity.class, (Object) body.getId(), map);
            //更新子表
            Map<String, Object> map0 = new HashMap<>();
            map0.put("bucket", body.getBucket());
            map0.put("accessKeyId", body.getAccessKeyId());
            map0.put("accessKeySecret", body.getAccessKeySecret());
            this.sessionFactory.update(session, MinioEntity.class, (Object) body.getId(), map0);
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
     * 获取Minio数据
     *
     * @param id         主键id
     * @param childTable 是否获取子表数据
     * @return
     */
    @RequestMapping(value = "/db2b7064-caa3-4143-a25b-f58221cea346")
    public DataResponse getMinio(@RequestParam String id, boolean childTable) {
        DataResponse dr = new DataResponse();
        try {
            Map<String, Object> map = new HashMap<>();
            if (childTable) {
                MinioEntity entity = this.sessionFactory.findById(MinioEntity.class, id);
                map.put("id", entity.getId());
                map.put("bucket", entity.getBucket());
                map.put("accessKeyId", entity.getAccessKeyId());
                map.put("accessKeySecret", entity.getAccessKeySecret());
            } else {
                //获取主表数据
                ResourceContainerEntity entity = this.sessionFactory.findById(ResourceContainerEntity.class, id);
                map.put("id", entity.getId());
                map.put("name", entity.getName());
                map.put("endpoint", entity.getEndpoint());
                map.put("provider", entity.getProvider());
                map.put("type", entity.getType());
                map.put("status", entity.getStatus());
                map.put("createTime", entity.getCreateTime());
                //读取子表数据
                MinioEntity child = this.sessionFactory.findById(MinioEntity.class, id);
                map.put("bucket", child.getBucket());
                map.put("accessKeyId", child.getAccessKeyId());
                map.put("accessKeySecret", child.getAccessKeySecret());
            }
            dr.setData(map);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }
    /**
     * 添加Minio
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/3dc7755f-234d-4540-9008-7837a796c291")
    public DataResponse addMinio(@Valid @RequestBody MinioBody body) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.exists(session, ResourceContainerEntity.class, new String[]{"name"}, new Object[]{body.getName()})) {
                transaction.commit();
                dr.setError(1, "重复");
                return dr;
            }
            //初始化主表数据
            ResourceContainerEntity entity = new ResourceContainerEntity();
            entity.setId(generator.generateId().toString());
            entity.setCreateTime(new Date());
            entity.setEndpoint(body.getEndpoint());
            entity.setStatus(body.getStatus());
            entity.setName(body.getName());
            entity.setProvider((short) 4);//设置服务商，4为Minio
            entity.setType(0);//存储类型，0表实任何类型存储
            //初始化Minio子表数据
            MinioEntity cosEntity = new MinioEntity();
            cosEntity.setId(entity.getId());
            cosEntity.setBucket(body.getBucket());
            cosEntity.setAccessKeyId(body.getAccessKeyId());
            cosEntity.setAccessKeySecret(body.getAccessKeySecret());
            session.save(entity);
            session.save(cosEntity);
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
     * 获取TencentCloudCos数据
     *
     * @param id         主键id
     * @param childTable 是否获取子表数据
     * @return
     */
    @RequestMapping(value = "/ea5890c9-57f3-461c-bc45-18e6fb4ac2f9")
    public DataResponse getTencentCloudCos(@RequestParam String id, boolean childTable) {
        DataResponse dr = new DataResponse();
        try {
            Map<String, Object> map = new HashMap<>();
            if (childTable) {
                TencentCloudCosEntity entity = this.sessionFactory.findById(TencentCloudCosEntity.class, id);
                map.put("id", entity.getId());
                map.put("bucket", entity.getBucket());
                map.put("accessKeyId", entity.getAccessKeyId());
                map.put("accessKeySecret", entity.getAccessKeySecret());
                map.put("region", entity.getRegion());
                map.put("trafficLimit", entity.getTrafficLimit());
            } else {
                //获取主表数据
                ResourceContainerEntity entity = this.sessionFactory.findById(ResourceContainerEntity.class, id);
                map.put("id", entity.getId());
                map.put("name", entity.getName());
                map.put("endpoint", entity.getEndpoint());
                map.put("provider", entity.getProvider());
                map.put("type", entity.getType());
                map.put("status", entity.getStatus());
                map.put("createTime", entity.getCreateTime());
                //读取子表数据
                TencentCloudCosEntity child = this.sessionFactory.findById(TencentCloudCosEntity.class, id);
                map.put("bucket", child.getBucket());
                map.put("accessKeyId", child.getAccessKeyId());
                map.put("accessKeySecret", child.getAccessKeySecret());
                map.put("region", child.getRegion());
                map.put("trafficLimit", child.getTrafficLimit());
            }
            dr.setData(map);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }
    /**
     * 添加TencentCloudCos
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/0c9f4c61-36f5-4131-aa8b-94ec81432e68")
    public DataResponse addTencentCloudCos(@Valid @RequestBody TencentCloudCosBody body) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.exists(session, ResourceContainerEntity.class, new String[]{"name"}, new Object[]{body.getName()})) {
                transaction.commit();
                dr.setError(1, "重复");
                return dr;
            }
            //初始化主表数据
            ResourceContainerEntity entity = new ResourceContainerEntity();
            entity.setId(generator.generateId().toString());
            entity.setCreateTime(new Date());
            entity.setEndpoint(body.getEndpoint());
            entity.setStatus(body.getStatus());
            entity.setName(body.getName());
            entity.setProvider((short) 1);//设置服务商，1为TencentCloudCos
            entity.setType(0);//存储类型，0表实任何类型存储
            //初始化TencentCloudCos子表数据
            TencentCloudCosEntity cosEntity = new TencentCloudCosEntity();
            cosEntity.setId(entity.getId());
            cosEntity.setBucket(body.getBucket());
            cosEntity.setAccessKeyId(body.getAccessKeyId());
            cosEntity.setAccessKeySecret(body.getAccessKeySecret());
            cosEntity.setRegion(body.getRegion());
            cosEntity.setTrafficLimit(body.getTrafficLimit());
            session.save(entity);
            session.save(cosEntity);
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
     * 修改TencentCloudCos
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/c9707bfe-3c6f-4852-901b-dd6971efe17d")
    public DataResponse editTencentCloudCos(@Valid @RequestBody TencentCloudCosBody body) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.notIdExists(session, ResourceContainerEntity.class, body.getId(), "name", body.getName())) {
                transaction.commit();
                dr.setError(1, "重复");
                return dr;
            }
            //更新主表
            Map<String, Object> map = new HashMap<>();
            map.put("name", body.getName());
            map.put("status", body.getStatus());
            map.put("endpoint", body.getEndpoint());
            map.put("type", body.getType());
            this.sessionFactory.update(session, ResourceContainerEntity.class, (Object) body.getId(), map);
            //更新子表
            Map<String, Object> map0 = new HashMap<>();
            map0.put("bucket", body.getBucket());
            map0.put("accessKeyId", body.getAccessKeyId());
            map0.put("accessKeySecret", body.getAccessKeySecret());
            map0.put("region", body.getRegion());
            map0.put("trafficLimit", body.getTrafficLimit());
            this.sessionFactory.update(session, TencentCloudCosEntity.class, (Object) body.getId(), map0);
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
     * 修改AliyunOss
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/348a1d8f-0568-4d01-89d0-503369747a07")
    public DataResponse editAliyunOss(@Valid @RequestBody AliyunOssBody body) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.notIdExists(session, ResourceContainerEntity.class, body.getId(), "name", body.getName())) {
                transaction.commit();
                dr.setError(1, "重复");
                return dr;
            }
            //更新主表
            Map<String, Object> map = new HashMap<>();
            map.put("name", body.getName());
            map.put("status", body.getStatus());
            map.put("endpoint", body.getEndpoint());
            map.put("type", body.getType());
            this.sessionFactory.update(session, ResourceContainerEntity.class, (Object) body.getId(), map);
            //更新子表
            Map<String, Object> map0 = new HashMap<>();
            map0.put("bucket", body.getBucket());
            map0.put("accessKeyId", body.getAccessKeyId());
            map0.put("accessKeySecret", body.getAccessKeySecret());
            this.sessionFactory.update(session, AliyunOssEntity.class, (Object) body.getId(), map0);
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
     * 添加AliyunOss
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/72234965-4192-46e8-94c0-7e93c188e1f7")
    public DataResponse addAliyunOss(@Valid @RequestBody AliyunOssBody body) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.exists(session, ResourceContainerEntity.class, new String[]{"name"}, new Object[]{body.getName()})) {
                transaction.commit();
                dr.setError(1, "重复");
                return dr;
            }
            //初始化主表数据
            ResourceContainerEntity entity = new ResourceContainerEntity();
            entity.setId(generator.generateId().toString());
            entity.setCreateTime(new Date());
            entity.setEndpoint(body.getEndpoint());
            entity.setStatus(body.getStatus());
            entity.setName(body.getName());
            entity.setProvider((short) 0);//设置服务商，0为AliyunOss
            entity.setType(0);//存储类型，0表实任何类型存储
            //初始化AliyunOss子表数据
            AliyunOssEntity ossEntity = new AliyunOssEntity();
            ossEntity.setId(entity.getId());
            ossEntity.setBucket(body.getBucket());
            ossEntity.setAccessKeyId(body.getAccessKeyId());
            ossEntity.setAccessKeySecret(body.getAccessKeySecret());
            session.save(entity);
            session.save(ossEntity);
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
     * 获取AliyunOss数据
     *
     * @param id         主键id
     * @param childTable 是否获取子表数据
     * @return
     */
    @RequestMapping(value = "/9ede3264-7b6d-4b5b-b0b9-03c8959ca822")
    public DataResponse getAliyunOss(@RequestParam String id, boolean childTable) {
        DataResponse dr = new DataResponse();
        try {
            Map<String, Object> map = new HashMap<>();
            if (childTable) {
                AliyunOssEntity entity = this.sessionFactory.findById(AliyunOssEntity.class, id);
                map.put("id", entity.getId());
                map.put("bucket", entity.getBucket());
                map.put("accessKeyId", entity.getAccessKeyId());
                map.put("accessKeySecret", entity.getAccessKeySecret());
            } else {
                //获取主表数据
                ResourceContainerEntity entity = this.sessionFactory.findById(ResourceContainerEntity.class, id);
                map.put("id", entity.getId());
                map.put("name", entity.getName());
                map.put("endpoint", entity.getEndpoint());
                map.put("provider", entity.getProvider());
                map.put("type", entity.getType());
                map.put("status", entity.getStatus());
                map.put("createTime", entity.getCreateTime());
                //读取子表数据
                AliyunOssEntity child = this.sessionFactory.findById(AliyunOssEntity.class, id);
                map.put("bucket", child.getBucket());
                map.put("accessKeyId", child.getAccessKeyId());
                map.put("accessKeySecret", child.getAccessKeySecret());
            }
            dr.setData(map);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }
    /**
     * 获取Hdfs数据
     *
     * @param id         主键id
     * @param childTable 是否获取子表数据
     * @return
     */
    @RequestMapping(value = "/1a088d23-2b45-4444-aed6-c045b0a6f112")
    public DataResponse getHdfs(@RequestParam String id, boolean childTable) {
        DataResponse dr = new DataResponse();
        try {
            Map<String, Object> map = new HashMap<>();
            if (childTable) {
                HdfsEntity entity = this.sessionFactory.findById(HdfsEntity.class, id);
                map.put("id", entity.getId());
                map.put("userName", entity.getUserName());
            } else {
                //获取主表数据
                ResourceContainerEntity entity = this.sessionFactory.findById(ResourceContainerEntity.class, id);
                map.put("id", entity.getId());
                map.put("name", entity.getName());
                map.put("endpoint", entity.getEndpoint());
                map.put("provider", entity.getProvider());
                map.put("type", entity.getType());
                map.put("status", entity.getStatus());
                map.put("createTime", entity.getCreateTime());
                //读取子表数据
                HdfsEntity child = this.sessionFactory.findById(HdfsEntity.class, id);
                map.put("userName", child.getUserName());
            }
            dr.setData(map);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }

    /**
     * 添加Hdfs
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/c3654c1d-b9f5-454f-b3f1-ac667ebe2e52")
    public DataResponse addHdfs(@Valid @RequestBody HdfsBody body) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.exists(session, ResourceContainerEntity.class, new String[]{"name"}, new Object[]{body.getName()})) {
                transaction.commit();
                dr.setError(1, "重复");
                return dr;
            }
            //初始化主表数据
            ResourceContainerEntity entity = new ResourceContainerEntity();
            entity.setId(generator.generateId().toString());
            entity.setCreateTime(new Date());
            entity.setEndpoint(body.getEndpoint());
            entity.setStatus(body.getStatus());
            entity.setName(body.getName());
            entity.setProvider((short) 3);//设置服务商，3为Hadoop HDFS
            entity.setType(0);//存储类型，0表实任何类型存储
            //初始化Hadoop HDFS子表数据
            HdfsEntity hdfsEntity = new HdfsEntity();
            hdfsEntity.setId(entity.getId());
            hdfsEntity.setUserName(body.getUserName());
            session.save(entity);
            session.save(hdfsEntity);
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
     * 修改Hdfs
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/a4dbdb2b-3008-4a9b-9c7d-becf5624cfb7")
    public DataResponse editHdfs(@Valid @RequestBody HdfsBody body) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.notIdExists(session, ResourceContainerEntity.class, body.getId(), "name", body.getName())) {
                transaction.commit();
                dr.setError(1, "重复");
                return dr;
            }
            //更新主表
            Map<String, Object> map = new HashMap<>();
            map.put("name", body.getName());
            map.put("status", body.getStatus());
            map.put("endpoint", body.getEndpoint());
            map.put("type", body.getType());
            this.sessionFactory.update(session, ResourceContainerEntity.class, (Object) body.getId(), map);
            //更新子表
            Map<String, Object> map0 = new HashMap<>();
            map0.put("userName", body.getUserName());
            this.sessionFactory.update(session, HdfsEntity.class, (Object) body.getId(), map0);
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
     * 添加FastDFS服务
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/b37ba8ae-bf4b-4dc9-a00e-8ae5fbd64a3a")
    public DataResponse addFastDFS(@Valid @RequestBody FastDFSBody body) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.exists(session, ResourceContainerEntity.class, new String[]{"name"}, new Object[]{body.getName()})) {
                transaction.commit();
                dr.setError(1, "重复");
                return dr;
            }
            //初始化主表数据
            ResourceContainerEntity entity = new ResourceContainerEntity();
            entity.setId(generator.generateId().toString());
            entity.setCreateTime(new Date());
            entity.setEndpoint(body.getEndpoint());
            entity.setStatus(body.getStatus());
            entity.setName(body.getName());
            entity.setProvider((short) 2);//设置服务商，2为FastDFS
            entity.setType(0);//存储类型，0表实任何类型存储
            //初始化FastDFS子表数据
            FastDFSEntity fastDFSEntity = new FastDFSEntity();
            fastDFSEntity.setId(entity.getId());
            fastDFSEntity.setConnectTimeout(body.getConnectTimeout());
            fastDFSEntity.setSoTimeout(body.getSoTimeout());
            session.save(entity);
            session.save(fastDFSEntity);
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
     * 修改FastDFS
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/27ad259a-e7c3-4090-888d-290ad9443eb6")
    public DataResponse editFastDFS(@Valid @RequestBody FastDFSBody body) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (HibernateUtils.notIdExists(session, ResourceContainerEntity.class, body.getId(), "name", body.getName())) {
                transaction.commit();
                dr.setError(1, "重复");
                return dr;
            }
            //更新主表
            Map<String, Object> map = new HashMap<>();
            map.put("name", body.getName());
            map.put("status", body.getStatus());
            map.put("endpoint", body.getEndpoint());
            map.put("type", body.getType());
            this.sessionFactory.update(session, ResourceContainerEntity.class, (Object) body.getId(), map);
            //更新子表
            Map<String, Object> map0 = new HashMap<>();
            map0.put("soTimeout", body.getSoTimeout());
            map0.put("connectTimeout", body.getConnectTimeout());
            this.sessionFactory.update(session, FastDFSEntity.class, (Object) body.getId(), map0);
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
     * 获取FastDFS数据
     *
     * @param id         主键id
     * @param childTable 是否获取子表数据
     * @return
     */
    @RequestMapping(value = "/ec549b5a-3fef-4525-9e55-1b41ca7781e6")
    public DataResponse getFastDFS(@RequestParam String id, boolean childTable) {
        DataResponse dr = new DataResponse();
        try {
            Map<String, Object> map = new HashMap<>();
            if (childTable) {
                FastDFSEntity entity = this.sessionFactory.findById(FastDFSEntity.class, id);
                map.put("id", entity.getId());
                map.put("soTimeout", entity.getSoTimeout());
                map.put("connectTimeout", entity.getConnectTimeout());
            } else {
                //获取主表数据
                ResourceContainerEntity entity = this.sessionFactory.findById(ResourceContainerEntity.class, id);
                map.put("id", entity.getId());
                map.put("name", entity.getName());
                map.put("endpoint", entity.getEndpoint());
                map.put("provider", entity.getProvider());
                map.put("type", entity.getType());
                map.put("status", entity.getStatus());
                map.put("createTime", entity.getCreateTime());
                //读取子表数据
                FastDFSEntity child = this.sessionFactory.findById(FastDFSEntity.class, id);
                map.put("soTimeout", child.getSoTimeout());
                map.put("connectTimeout", child.getConnectTimeout());
            }
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
    @RequestMapping(value = "/2305ad8c-094c-46b9-ac95-8bbbe5f4313f")
    public DataResponse delete(@RequestParam String id) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //删除主表
            this.sessionFactory.deleteById(session, ResourceContainerEntity.class, "id", id);
            //删除子表
            this.sessionFactory.deleteById(session, AliyunOssEntity.class, "id", id);
            this.sessionFactory.deleteById(session, FastDFSEntity.class, "id", id);
            this.sessionFactory.deleteById(session, HdfsEntity.class, "id", id);
            this.sessionFactory.deleteById(session, TencentCloudCosEntity.class, "id", id);
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
    @RequestMapping(value = "/8993afcf-ca2f-4475-bfe6-5b242bcb1a08")
    public DataResponse statusUpdate(@RequestParam String id, @RequestParam int status) {
        DataResponse dr = new DataResponse();
        try {
            this.sessionFactory.update(ResourceContainerEntity.class, id, "status", status);
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
    @RequestMapping(value = "/106c4c47-5589-42de-81af-e40322444664")
    public DataResponse batchDelete(@RequestBody RequestIds ids) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //先删除管理员
            this.sessionFactory.deleteByIds(session, ResourceContainerEntity.class, "id", ids.getIds());
            //删除子表
            this.sessionFactory.deleteByIds(session, AliyunOssEntity.class, "id", ids.getIds());
            this.sessionFactory.deleteByIds(session, FastDFSEntity.class, "id", ids.getIds());
            this.sessionFactory.deleteByIds(session, HdfsEntity.class, "id", ids.getIds());
            this.sessionFactory.deleteByIds(session, TencentCloudCosEntity.class, "id", ids.getIds());
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
     * 扩展查询请求
     */
    public static class SelectRequestEx extends SelectRequest {
        private int provider = -1;

        public void setProvider(int provider) {
            this.provider = provider;
        }

        public int getProvider() {
            return provider;
        }
    }

    /**
     * 获取分页数据
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/f6be73c4-7e08-48c6-95d8-44f78b972d80")
    public SelectResponse select(@RequestBody SelectRequestEx request) {
        SelectResponse dr = new SelectResponse();
        try (Session session = this.sessionFactory.openSession()) {
            DetachedCriteria criteria = DetachedCriteria.forClass(ResourceContainerEntity.class);
            if (!StringUtils.isEmpty(request.getKey())) {
                criteria.add(Restrictions.like("name", request.getKey(), MatchMode.ANYWHERE));
            }
            if (!StringUtils.isEmpty(request.getKey())) {
                criteria.add(Restrictions.like("endpoint", request.getKey(), MatchMode.ANYWHERE));
            }
            if (request.getStartTime() != null && request.getEndTime() == null) {
                criteria.add(Restrictions.ge("createTime", request.getStartTime()));
            }
            if (request.getStartTime() != null && request.getEndTime() != null) {
                criteria.add(Restrictions.between("createTime", request.getStartTime(), request.getEndTime()));
            }
            if (request.provider != -1) {
                criteria.add(Restrictions.eq("provider", new Integer(request.provider).shortValue()));
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
                    new String[]{"id", "name", "endpoint", "provider", "type", "provider", "status", "createTime"});
            dr.setData(maps);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }
}