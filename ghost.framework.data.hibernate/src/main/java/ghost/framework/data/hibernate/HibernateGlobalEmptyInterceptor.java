package ghost.framework.data.hibernate;

import ghost.framework.context.collections.generic.ISmartList;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * package: ghost.framework.data.hibernate
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:删除拦截器
 * {@link EmptyInterceptor}
 * {@link EmptyInterceptor#onDelete(Object, Serializable, Object[], String[], Type[])}
 * @Date: 2020/8/22:7:41
 */
public class HibernateGlobalEmptyInterceptor extends EmptyInterceptor implements ISmartList<IHibernateInterceptor> {
    private Logger log = LoggerFactory.getLogger(HibernateGlobalEmptyInterceptor.class);
    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        log.info("删除数据 onDelete(" + entity.toString() + "," + id.toString() + "))");
        for (IHibernateInterceptor interceptor : list) {
            //删除约束检查
            if (interceptor instanceof IHibernateDeleteConstraintInterceptor) {
                IHibernateDeleteConstraintInterceptor deleteInterceptor = (IHibernateDeleteConstraintInterceptor) interceptor;
                if (deleteInterceptor.deleteConstraint(entity, id)) {
                    //不删除
                    return;
                }
            }
        }
        super.onDelete(entity, id, state, propertyNames, types);
    }

    private List<IHibernateInterceptor> list = new ArrayList<>();

    @Override
    public boolean add(IHibernateInterceptor interceptor) {
        synchronized (list) {
            return list.add(interceptor);
        }
    }

    @Override
    public boolean remove(IHibernateInterceptor interceptor) {
        synchronized (list) {
            return list.remove(interceptor);
        }
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean contains(IHibernateInterceptor interceptor) {
        synchronized (list) {
            return list.contains(interceptor);
        }
    }
}