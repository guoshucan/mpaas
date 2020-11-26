package ghost.framework.data.hibernate;

import java.io.Serializable;

/**
 * package: ghost.framework.data.hibernate
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:删除拦截器接口
 * @Date: 2020/8/22:7:54
 */
public interface IHibernateDeleteConstraintInterceptor extends IHibernateInterceptor {
    /**
     * 删除约束检查
     *
     * @param entity 删除实体
     * @param id     删除主键
     * @return
     */
    boolean deleteConstraint(Object entity, Serializable id);
}