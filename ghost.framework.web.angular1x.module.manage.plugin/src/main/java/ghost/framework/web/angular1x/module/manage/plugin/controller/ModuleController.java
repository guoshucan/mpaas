package ghost.framework.web.angular1x.module.manage.plugin.controller;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.module.IModule;
import ghost.framework.context.module.IModuleContainer;
import ghost.framework.util.ReflectUtil;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.angular1x.module.manage.plugin.entitys.ModuleEntity;
import ghost.framework.web.angular1x.module.manage.plugin.entitys.RepositoryModuleEntity;
import ghost.framework.web.context.bind.annotation.*;
import ghost.framework.web.context.http.multipart.MultipartFile;
import ghost.framework.web.context.http.requestContent.SelectRequest;
import ghost.framework.web.context.http.responseContent.DataResponse;
import ghost.framework.web.context.http.responseContent.SelectResponse;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.hibernate.Session;
import org.hibernate.criterion.*;

import java.util.*;
import java.util.jar.Manifest;

/**
 * package: ghost.framework.web.angular1x.module.manage.plugin.controller
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块控制器
 * @Date: 2020/4/21:11:07
 */
@RestController("/api/module/management")
public class ModuleController extends ControllerBase {
    /**
     * 添加模块
     * @param file
     * @return
     */
    @RequestMapping(value = "/782a589f-3df9-488b-b65d-7d3effe31295")
    public DataResponse add(@RequestParam("file") MultipartFile file) {
        DataResponse dr = new DataResponse();
        try {
//            ModuleEntity entity = this.sessionFactory.openGet(ModuleEntity.class, groupId);
//            Map<String, Object> map = new HashMap<>();
//            map.put("groupId", entity.getGroupId());
//            map.put("groupName", entity.getGroupId());
//            map.put("status", entity.getStatus());
//            map.put("createTime", entity.getCreateTime());
//            dr.setData(map);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }
    public static class FileArtifactInternal {
        private String groupId;
        private String artifactId;
        private String version;

        public String getArtifactId() {
            return artifactId;
        }

        public String getGroupId() {
            return groupId;
        }

        public String getVersion() {
            return version;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public void setArtifactId(String artifactId) {
            this.artifactId = artifactId;
        }

        private int internal;

        public void setInternal(int internal) {
            this.internal = internal;
        }

        public int getInternal() {
            return internal;
        }
    }

    /**
     * 获取jar信息
     * @param artifact
     * @return
     */
    @RequestMapping(value = "/3eeb2215-469f-459d-854f-3ebb0956d345")
    public DataResponse get(@RequestBody FileArtifactInternal artifact) {
        DataResponse dr = new DataResponse();
        try {
            Map map = new LinkedHashMap();
            Manifest manifest = null;
            //判断是否微内置模块
            if (artifact.internal == 0) {
                //获取模块
                IModule module = this.moduleContainer.getModule(new DefaultArtifact(artifact.groupId, artifact.artifactId, null, artifact.version));
                map.put("groupId", module.getArtifact().getGroupId());
                map.put("artifactId", module.getArtifact().getArtifactId());
                map.put("version", module.getArtifact().getVersion());
                manifest = ReflectUtil.getManifest(module.getArtifact().getFile());
                dr.setData(map);
            } else if (artifact.internal == 1) {
                //获取动态模块;
                ModuleEntity entity = this.sessionFactory.get(
                        ModuleEntity.class,
                        new String[]{"groupId", "artifactId", "version"},
                        new Object[]{artifact.groupId, artifact.artifactId, artifact.version}
                );
                map.put("groupId", entity.getGroupId());
                map.put("artifactId", entity.getArtifactId());
                map.put("version", entity.getVersion());

            } else if (artifact.internal == 2) {
                //获取动态模块;
                RepositoryModuleEntity entity = this.sessionFactory.get(
                        RepositoryModuleEntity.class,
                        new String[]{"groupId", "artifactId", "version"},
                        new Object[]{artifact.groupId, artifact.artifactId, artifact.version}
                );
                map.put("groupId", entity.getGroupId());
                map.put("artifactId", entity.getArtifactId());
                map.put("version", entity.getVersion());

            }
            //判断是否有程序包信息
            if (manifest == null) {
                map.put("attributes", manifest.getMainAttributes());
            } else {
                map.put("attributes", new LinkedHashMap());
            }
            dr.setData(map);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }

    public static class ModuleSelectRequest extends SelectRequest {
        public ModuleSelectRequest(){}
        private int internal;

        public void setInternal(int internal) {
            this.internal = internal;
        }

        public int getInternal() {
            return internal;
        }
    }

    /**
     * 注入模块容器接口
     */
    @Application
    @Autowired
    private IModuleContainer moduleContainer;

    /**
     * 获取模块列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/814db3c8-97d9-4274-9ce2-08cdbace1583", method = RequestMethod.POST)
    public SelectResponse select(@RequestBody ModuleSelectRequest request) {
        SelectResponse dr = new SelectResponse();
        //判断是否获取内置模块列表
        if (request.internal == 0) {
            try {
                List<Map> maps = new ArrayList<>();
                //
                for (Map.Entry<String, IModule> entry : this.moduleContainer.entrySet()) {
                    Map map = new HashMap();
                    map.put("groupId", entry.getValue().getArtifact().getGroupId());
                    map.put("artifactId", entry.getValue().getArtifact().getArtifactId());
                    map.put("version", entry.getValue().getArtifact().getVersion());
                    maps.add(map);
                }
                dr.setData(maps);
            } catch (Exception e) {
                this.exception(e);
                dr.setError(-1, e.getMessage());
            }
            return dr;
        }
        try (Session session = this.sessionFactory.openSession()) {
            DetachedCriteria criteria = DetachedCriteria.forClass(ModuleEntity.class);
            if (!StringUtils.isEmpty(request.getKey())) {
                criteria.add(Restrictions.or(new SimpleExpression[] {
                        Restrictions.like("groupId", request.getKey(), MatchMode.ANYWHERE),
                        Restrictions.like("artifactId", request.getKey(), MatchMode.ANYWHERE),
                        Restrictions.like("version", request.getKey(), MatchMode.ANYWHERE)
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
                    new String[]{"groupId", "artifactId", "version", "description", "status", "createTime"});
            dr.setData(maps);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }
}