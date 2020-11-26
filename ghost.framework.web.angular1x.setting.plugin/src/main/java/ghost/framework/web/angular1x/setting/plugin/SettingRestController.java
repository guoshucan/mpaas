package ghost.framework.web.angular1x.setting.plugin;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.util.IdGenerator;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.context.bind.annotation.RequestBody;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.bind.annotation.RequestParam;
import ghost.framework.web.context.bind.annotation.RestController;
import ghost.framework.web.context.http.responseContent.DataResponse;
import ghost.framework.web.setting.entity.WebSettingEntity;
import ghost.framework.web.setting.entity.WebSettingLogoEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * package: ghost.framework.web.angular1x.Setting.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/8:12:30
 */
@RestController("/api")
public class SettingRestController extends ControllerBase {
    /**
     * 注入id生成接口
     */
    @Application
    @Autowired
    private IdGenerator generator;
    @RequestMapping(value = "/e1e1e89a-a070-4878-8a81-afa631f48fe9")
    public DataResponse edit(@Valid @RequestBody WebSettingEntity entity) {
        DataResponse dr = new DataResponse();
        Map<String, Object> map = new HashMap<>();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            //判断是否存在
            if (this.sessionFactory.exists(session, WebSettingEntity.class, "locale", entity.getLocale())) {
                map.put("url", entity.getUrl());
                map.put("title", entity.getTitle());
                map.put("copyright", entity.getCopyright());
                map.put("editTime", new Date());
                this.sessionFactory.update(session, WebSettingEntity.class, (Object) entity.getLocale(), map);
            } else {
                entity.setEditTime(new Date());
                session.save(entity);
            }
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
     * @param locale
     * @return
     */
    @RequestMapping(value = "/1c833c07-bc54-41cf-af0e-cf34c8262570")
    public DataResponse get(@RequestParam String locale) {
        DataResponse dr = new DataResponse();
        try {
            WebSettingEntity settingEntity = this.sessionFactory.get(WebSettingEntity.class, "locale", locale);
            WebSettingLogoEntity settingLogoEntity = this.sessionFactory.get(WebSettingLogoEntity.class);
            Map<String, Object> map = new HashMap<>();
            if (settingLogoEntity == null) {
                map.put("logoUrl", null);
                map.put("logo", null);
            } else {
                map.put("logoUrl", settingLogoEntity.getLogoUrl());
                map.put("logo", settingLogoEntity.getLogo());
            }
            if (settingEntity == null) {
//                map.put("id", null);
                map.put("copyright", null);
                map.put("locale", null);
                map.put("title", null);
                map.put("url", null);
            } else {
//                map.put("id", settingEntity.getId());
                map.put("copyright", settingEntity.getCopyright());
                map.put("locale", settingEntity.getLocale());
                map.put("title", settingEntity.getTitle());
                map.put("url", settingEntity.getUrl());
            }
            dr.setData(map);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }
}