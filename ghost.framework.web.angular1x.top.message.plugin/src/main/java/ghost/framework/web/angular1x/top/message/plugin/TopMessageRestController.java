package ghost.framework.web.angular1x.top.message.plugin;
import ghost.framework.web.angular1x.context.controller.RestControllerBase;
import ghost.framework.web.context.bind.annotation.RequestBody;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.bind.annotation.RequestParam;
import ghost.framework.web.context.bind.annotation.RestController;
import ghost.framework.web.context.http.requestContent.RequestIds;
import ghost.framework.web.context.http.requestContent.SelectRequest;
import ghost.framework.web.context.http.responseContent.DataResponse;
import ghost.framework.web.context.http.responseContent.SelectResponse;
import ghost.framework.web.top.message.entity.TopMessageEntity;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import java.util.List;
import java.util.Map;

/**
 * package: ghost.framework.web.angular1x.top.message.plugin.controller
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/7:22:53
 */
@RestController("/api")
public class TopMessageRestController extends RestControllerBase {
    /**
     * @param id
     * @return
     */
    @RequestMapping(value = "/82ebea43-72ce-4d52-b6e7-4a35be015ef1")
    public DataResponse get(@RequestParam String id) {
        DataResponse dr = new DataResponse();
        Transaction transaction = null;
        try (Session session = this.sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            this.sessionFactory.update(session, TopMessageEntity.class, id, "status", true);
            dr.setData(this.sessionFactory.get(session, TopMessageEntity.class, id));
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
    @RequestMapping(value = "/a6d48a43-ac96-4d78-841d-a0514c4bd048")
    public DataResponse delete(@RequestParam String id) {
        DataResponse dr = new DataResponse();
        try {
            this.sessionFactory.openDelete(TopMessageEntity.class, id);
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
    @RequestMapping(value = "/8b883d4a-ddc1-4f59-a21c-c782abecc3e2")
    public DataResponse batchDelete(@RequestBody RequestIds ids) {
        DataResponse dr = new DataResponse();
        try {
            this.sessionFactory.openDelete(TopMessageEntity.class, "id", ids.getIds());
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
    @RequestMapping(value = "/0915139c-73b9-450f-a930-38f7fbb68de2")
    public SelectResponse select(@RequestBody SelectRequest request) {
        SelectResponse dr = new SelectResponse();
        try (Session session = this.sessionFactory.openSession()) {
            DetachedCriteria criteria = DetachedCriteria.forClass(TopMessageEntity.class);
            if (!StringUtils.isEmpty(request.getKey())) {
                criteria.add(Restrictions.like("title", request.getKey(), MatchMode.ANYWHERE));
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
                    new String[]{"id", "title", "status", "url", "createTime", "icon", "content"});
            dr.setData(maps);
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }
}