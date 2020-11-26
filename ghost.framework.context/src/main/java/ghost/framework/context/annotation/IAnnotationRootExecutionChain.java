package ghost.framework.context.annotation;

import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.context.application.IApplication;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

/**
 * package: ghost.framework.core.annotation
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注释根执行链接口
 * @Date: 2020/2/17:0:41
 * @param <K> 注释类型
 * @param <V> 注释链对象
 * @param <L> 注释链依赖列表
 */
public interface IAnnotationRootExecutionChain
        <
        K extends Class<? extends Annotation>,
        V extends IAnnotationExecutionChain<K, V, L>,
        L extends List<V>
        >
        extends Map<V, L> {
    /**
     * 删除排除注释
     *
     * @param k 排除注释
     * @return
     */
    boolean removeExclusion(K k);

    /**
     * 添加排除注释
     *
     * @param k 排除注释
     * @return
     */
    boolean addExclusion(K k);

    /**
     * 获取应用接口
     *
     * @return
     */
    IApplication getApp();

    /**
     * 删除注释
     *
     * @param key
     * @return
     */
    V remove(K key);

    /**
     * 添加数组注释
     *
     * @param keys 数组注释
     * @return
     */
    V[] add(K[] keys);

    /**
     * 添加默认域注释
     *
     * @param keys 注释类型
     * @param tag  注释标签
     * @return 返回添加创建对象
     */
    V[] add(K[] keys, AnnotationTag.AnnotationTags tag);

    /**
     * 添加指定域注释
     *
     * @param keys   数组注释类型
     * @param tag    注释标签
     * @param domain 注释域
     * @return 返回添加创建对象
     */
    V[] add(K[] keys, AnnotationTag.AnnotationTags tag, Object domain);

    /**
     * 添加注释
     *
     * @param key 注释类型
     * @return
     */
    V add(K key);

    /**
     * 添加注释
     *
     * @param key 注释类型
     * @param tag 注释标签
     * @return
     */
    V add(K key, AnnotationTag.AnnotationTags tag);

    /**
     * 添加指定域注释
     *
     * @param key    注释类型
     * @param tag    注释标签
     * @param domain 注释域
     * @return
     */
    V add(K key, AnnotationTag.AnnotationTags tag, Object domain);
    /**
     * 添加指定域注释
     *
     * @param key    注释类型
     * @param single 该类型注释是否为单实例
     * @param tag    注释标签
     * @param domain 注释域
     * @return 返回添加创建对象
     */
    V add(K key, boolean single, AnnotationTag.AnnotationTags tag, Object domain);
    /**
     * 添加指定域注释依赖
     *
     * @param key    要添加依赖的注释
     * @param keys   数组依赖注释类型
     * @param tag    组数依赖注释标签
     * @param domain 数组依赖注释域
     * @return
     */
    V addDepend(K key, K[] keys, AnnotationTag.AnnotationTags tag, Object domain);

    /**
     * 添加注释依赖
     *
     * @param key  要添加依赖的注释
     * @param keys 数组依赖注释类型
     * @param tag  组数依赖注释标签
     * @return
     */
    V addDepend(K key, K[] keys, AnnotationTag.AnnotationTags tag);

    /**
     * 添加注释依赖
     *
     * @param key  要添加依赖的注释
     * @param keys 数组依赖注释类型
     * @return
     */
    V[] addDepend(K[] key, K[] keys);

    /**
     * 添加注释依赖
     *
     * @param key  要添加依赖的注释
     * @param keys 数组依赖注释类型
     * @return
     */
    V addDepend(K key, K[] keys);

    /**
     * 添加指定域依赖注释
     *
     * @param key    数组要添加依赖的注释
     * @param keys   数组依赖注释类型
     * @param tag    组数依赖注释标签
     * @param domain 数组依赖注释域
     * @return
     */
    V[] addDepend(K[] key, K[] keys, AnnotationTag.AnnotationTags tag, Object domain);

    /**
     * 添加指定域依赖注释
     *
     * @param key  数组要添加依赖的注释
     * @param keys 数组依赖注释类型
     * @param tag  组数依赖注释标签
     * @return
     */
    V[] addDepend(K[] key, K[] keys, AnnotationTag.AnnotationTags tag);

    /**
     * 查找域列表
     *
     * @param domain
     * @return
     */
    List<V> findDomainList(Object domain);

    /**
     * 删除域
     *
     * @param domain 域对象
     * @return 是否有删除的域
     */
    boolean removeDomain(Object domain);

    /**
     * 获取注释链
     *
     * @param annotations 素组注释，可以是类型，可以是函数，可以是参数，可以是构建，可以是声明，可以是包等的数组注释
     * @return
     */
    Map<K, ExecutionAnnotation<K, V, L>> getExecutionChain(Annotation[] annotations);

    /**
     * 获取注释链
     *
     * @param target
     * @return
     */
    Map<K, ExecutionAnnotation<K, V, L>> getExecutionChain(Constructor target);

    /**
     * 获取注释链
     *
     * @param target
     * @return
     */
    Map<K, ExecutionAnnotation<K, V, L>> getExecutionChain(Parameter target);

    /**
     * 获取注释链
     *
     * @param target
     * @return
     */
    Map<K, ExecutionAnnotation<K, V, L>> getExecutionChain(Method target);

    /**
     * 获取注释链
     *
     * @param target
     * @return
     */
    Map<K, ExecutionAnnotation<K, V, L>> getExecutionChain(Field target);

    /**
     * 获取注释链
     *
     * @param target
     * @return
     */
    Map<K, ExecutionAnnotation<K, V, L>> getExecutionChain(Class<?> target);

    /**
     * 获取注释链
     *
     * @param target
     * @return
     */
    Map<K, ExecutionAnnotation<K, V, L>> getExecutionChain(Package target);

    /**
     * 获取注释链
     *
     * @param annotations 素组注释，可以是类型，可以是函数，可以是参数，可以是构建，可以是声明，可以是包等的数组注释
     * @return
     */
    List<V> getExecutionChainList(Annotation[] annotations);
}