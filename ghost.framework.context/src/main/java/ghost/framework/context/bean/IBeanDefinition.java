package ghost.framework.context.bean;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.IGetDomain;
import ghost.framework.context.IGetPrimary;
import ghost.framework.context.module.IModule;

import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.beans
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:绑定定义接口
 * @Date: 2019/12/10:8:03
 */
public interface IBeanDefinition extends IGetDomain, IGetPrimary {
    /**
     * Override the target scope of this bean, specifying a new scope name.
     * @see #SCOPE_SINGLETON
     * @see #SCOPE_PROTOTYPE
     */
    default void setScope(@Nullable String scope){

    }

    /**
     * Return the name of the current target scope for this bean,
     * or {@code null} if not known yet.
     */
    @Nullable
    default String getScope(){
        return null;
    }
    /**
     * Return the property values to be applied to a new instance of the bean.
     * <p>The returned instance can be modified during bean factory post-processing.
     * @return the MutablePropertyValues object (never {@code null})
     */
    default MutablePropertyValues getPropertyValues(){
        return null;
    }
    /**
     * Return if there are property values values defined for this bean.
     * @since 5.0.2
     */
    default boolean hasPropertyValues() {
        return !getPropertyValues().isEmpty();
    }
    /**
     * Return the constructor argument values for this bean.
     * <p>The returned instance can be modified during bean factory post-processing.
     * @return the ConstructorArgumentValues object (never {@code null})
     */
   default ConstructorArgumentValues getConstructorArgumentValues(){
       return null;
   }
    /**
     * Return if there are constructor argument values defined for this bean.
     * @since 5.0.2
     */
    default boolean hasConstructorArgumentValues() {
        return !getConstructorArgumentValues().isEmpty();
    }
    /**
     * 获取绑定对象
     *
     * @return
     */
    Object getObject();

    /**
     * 获取所属域
     * Bean对象所属返回
     * 比如属于{@link IModule}或属于{@see PluginPackage}的对象域
     * 一般属于下面两种域对象
     * {@link IModule}
     * {@see PluginPackage}
     * @return
     */
    @Override
    default Object getDomain() {
        return null;
    }

    /**
     * 获取绑定key
     *
     * @return
     */
    String getName();

    /**
     * 获取绑定定义对象数组注释对象
     *
     * @return
     */
    Annotation[] getAnnotations();
    default String getBeanClassName(){
        return null;
    }
    @Override
    default boolean isPrimary(){
        return false;
    }

    default  boolean isLazyInit(){
        return false;
    }

   default Object getAttribute(String attribute){
        return false;
   }

    default void setAttribute(String attribute, Object target){

    }

    default boolean isAutowireCandidate(){
        return false;
    }

    default boolean isAbstract(){
        return false;
    }

    default  String getResourceDescription(){
        return null;
    }

    default String getParentName(){
        return null;
    }

    default void setPrimary(boolean primary){

    }

    default void setAutowireCandidate(boolean autowireCandidate){

    }
}