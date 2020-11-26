package ghost.framework.data.hibernate;

import ghost.framework.data.commons.IDataBaseProperties;

import java.util.List;

/**
 * package: ghost.framework.data.hibernate
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:Hibernate生成器接口
 * {see org.hibernate.EmptyInterceptor} interceptor {{@link #addInterceptor(IHibernateInterceptor)}}
 * {see org.hibernate.EmptyInterceptor} interceptor {{@link #removeInterceptor(IHibernateInterceptor)}}
 * @Date: 2020/3/22:16:36
 */
public interface IHibernateBuilder {
    /**
     * 添加拦截器
     * @param interceptor
     */
    void addInterceptor(IHibernateInterceptor interceptor);

    /**
     * 删除拦截器
     * @param interceptor
     */
    void removeInterceptor(IHibernateInterceptor interceptor);
    /**
     * 获取实体列表
     *
     * @return
     */
    List<Class<?>> getEntityList();

    /**
     * 重建数据源
     */
    void rebuild();

    /**
     * 添加实体
     *
     * @param entity 实体类型
     */
    void add(Class<?> entity);

    /**
     * 添加实体
     *
     * @param entitys 实体类型
     */
    void add(Class<?>[] entitys);

    /**
     * 删除实体
     *
     * @param entity 实体类型
     */
    void remove(Class<?> entity);

    /**
     * 删除实体
     *
     * @param entitys 实体类型
     */
    void remove(Class<?>[] entitys);

    /**
     * 获取数据库配置
     * @return
     */
    IDataBaseProperties getDataBaseProperties();

    /**
     * 设置数据库配置
     * @param dataBaseProperties
     */
    void setDataBaseProperties(IDataBaseProperties dataBaseProperties);
}