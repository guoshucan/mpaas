package ghost.framework.core.annotation;

import ghost.framework.beans.annotation.constructor.Constructor;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.context.annotation.IAnnotationExecutionChain;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.proxy.ProxyUtil;
import ghost.framework.util.Assert;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * package: ghost.framework.core.annotation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注释执行链
 * @Date: 2020/2/16:18:02
 */
public class AnnotationExecutionChain
        <K extends Class<? extends Annotation>, V extends IAnnotationExecutionChain<K, V, L>, L extends Collection<V>>
        implements IAnnotationExecutionChain<K, V, L> {
    /**
     * 初始化注释执行链
     *
     * @param annotationClass 注释类型
     * @param single          该类型注释是否为单实例
     * @param tag             该注释标签
     * @param domain          该注释所属域
     */
    public AnnotationExecutionChain(K annotationClass, boolean single, AnnotationTag.AnnotationTags tag, Object domain) {
        Assert.notNull(annotationClass, "AnnotationExecutionChain is annotationClass null error");
        Assert.notNull(tag, "AnnotationExecutionChain is tag null error");
        Assert.notNull(domain, "AnnotationExecutionChain is domain null error");
        this.annotationClass = annotationClass;
        this.single = single;
        this.tag = tag;
        this.domain = domain;
    }
    /**
     * 初始化注释执行链
     *
     * @param annotationClass 注释类型
     * @param tag             该注释标签
     * @param domain          该注释所属域
     */
    public AnnotationExecutionChain(K annotationClass, AnnotationTag.AnnotationTags tag, Object domain) {
        Assert.notNull(annotationClass, "AnnotationExecutionChain is annotationClass null error");
        Assert.notNull(domain, "AnnotationExecutionChain is domain null error");
        this.annotationClass = annotationClass;
        this.tag = tag;
        this.domain = domain;
    }
    /**
     * 初始化注释执行链
     *
     * @param annotationClass 注释类型
     * @param domain          该注释所属域
     */
    public AnnotationExecutionChain(K annotationClass, Object domain) {
        Assert.notNull(annotationClass, "AnnotationExecutionChain is annotationClass null error");
        Assert.notNull(domain, "AnnotationExecutionChain is domain null error");
        this.annotationClass = annotationClass;
        this.domain = domain;
    }
    /**
     * 初始化注释执行链
     *
     * @param annotationClass 注释类型
     * @param single          该类型注释是否为单实例
     * @param domain          该注释所属域
     */
    public AnnotationExecutionChain(K annotationClass, boolean single, Object domain) {
        Assert.notNull(annotationClass, "AnnotationExecutionChain is annotationClass null error");
        Assert.notNull(domain, "AnnotationExecutionChain is domain null error");
        this.annotationClass = annotationClass;
        this.single = single;
        this.domain = domain;
    }

    /**
     * 该类型注释是否为单实例
     */
    private boolean single;

    /**
     * 获取该类型注释是否为单实例
     *
     * @return
     */
    @Override
    public boolean isSingle() {
        return single;
    }

    /**
     * 该注释依赖列表
     */
    private L depend = (L) new ArrayList<V>();

    /**
     * 获取该注释依赖列表
     *
     * @return
     */
    @Override
    public L getDepend() {
        return depend;
    }

    /**
     * 注释标签
     */
    private AnnotationTag.AnnotationTags tag = AnnotationTag.AnnotationTags.Unknown;

    /**
     * 获取注释标签
     *
     * @return
     */
    @Override
    public AnnotationTag.AnnotationTags getTag() {
        return tag;
    }

    /**
     * 设置注释标签
     *
     * @param tag
     */
    @Override
    public void setTag(AnnotationTag.AnnotationTags tag) {
        this.tag = tag;
    }

    /**
     * 注释类型
     */
    private K annotationClass;

    /**
     * 类型构建
     *
     * @param application 应用接口
     */
    @Constructor
    public AnnotationExecutionChain(IApplication application) {
        app = application;
    }

    /**
     * 获取应用接口
     *
     * @return
     */
    @Override
    public IApplication getApp() {
        return app;
    }

    /**
     * 应用接口
     */
    private static IApplication app;

    /**
     * 获取注释类型
     *
     * @return
     */
    @Override
    public K getAnnotationClass() {
        return annotationClass;
    }

    /**
     * 设置域
     *
     * @param domain 域对象
     */
    @Override
    public void setDomain(Object domain) {
        Assert.notNull(domain, "setDomain is domain null error");
        this.domain = domain;
    }

    /**
     * 域对象
     */
    private Object domain;

    /**
     * 查找域列表
     *
     * @param domain
     * @return
     */
    @Override
    public List<V> findDomainList(Object domain) {
        Assert.notNull(domain, "findDomainList is domain null error");
        List<V> list = new ArrayList<>();
//        IOutAnnotation<K, V, L> out = new OutAnnotation<K, V, L>(domain, null) {
//            @Override
//            public boolean test(V out) {
//                if (out.getDomain() != null && out.getDomain().equals(this.getDomain())) {
//                    list.add(out);
//                }
//                return false;
//            }
//        };
//        //查找注释
//        isAnnotation(this, out);
        return list;
    }

    /**
     * 删除域
     *
     * @param domain 域对象
     * @return 是否有删除的域
     */
    @Override
    public boolean removeDomain(Object domain) {
        Assert.notNull(domain, "removeDomain is domain null error");
        //获取要删除域的列表
        List<V> list = this.findDomainList(domain);
        //遍历删除域
        list.forEach(new Consumer<V>() {
            /**
             * 删除操作
             * @param v
             */
            @Override
            public void accept(V v) {
                //判断是否为根目录
//                if (v.getParent() == null) {
//                    //删除根目录注释
//                    app.getBean(IAnnotationExecutionChainMap.class).remove(v.getAnnotationClass());
//                } else {
//                    //删除子目录
//                    v.getParent().remove(v.getAnnotationClass());
//                }
            }
        });
        //判断是否有删除域
        return list.size() > 0;
    }

    /**
     * 获取域
     *
     * @return 返回域对象
     */
    @Override
    public Object getDomain() {
        return domain;
    }

    /**
     * 查找注释
     *
     * @param annotation 注释对象
     * @return 返回找到注释
     */
    @Override
    public IAnnotationExecutionChain<K, V, L> findAnnotation(Annotation annotation) {
        Assert.notNull(annotation, "findAnnotation is annotation null error");
        return this.findAnnotation(null, (K) ProxyUtil.getProxyObjectAnnotationClass(annotation));
    }

    /**
     * 查找注释
     *
     * @param key 注释对象
     * @return 返回找到注释
     */
    @Override
    public IAnnotationExecutionChain<K, V, L> findAnnotation(K key) {
        Assert.notNull(key, "findAnnotation is annotation null error");
        return this.findAnnotation(null, key);
    }

    /**
     * 查找注释
     *
     * @param annotation 注释对象
     * @param domain     注释域
     * @return 返回找到注释
     */
    @Override
    public IAnnotationExecutionChain<K, V, L> findAnnotation(Object domain, Annotation annotation) {
        Assert.notNull(domain, "findAnnotation is domain null error");
        Assert.notNull(annotation, "findAnnotation is annotation null error");
        return this.findAnnotation(domain, (K) ProxyUtil.getProxyObjectAnnotationClass(annotation));
    }

    /**
     * 查找注释
     *
     * @param key    注释类型
     * @param domain 注释域
     * @return 返回找到注释
     */
    @Override
    public IAnnotationExecutionChain<K, V, L> findAnnotation(Object domain, K key) {
        Assert.isTrue(domain == null && key == null, "findAnnotation is null error");
//        Assert.notNull(domain, "findAnnotation is key null error");
        //初始化输出注释对象
//        OutAnnotation<K, V, L> out = new OutAnnotation<K, V, L>(domain, key) {
//            private V v;
//
//            @Override
//            public V getOut() {
//                return v;
//            }
//
//            @Override
//            public boolean test(V out) {
//                if ((this.getDomain() == null && out.getAnnotationClass().equals(this.getKey())) || (Objects.equals(this.getDomain(), out.getDomain()) && out.getAnnotationClass().equals(this.getKey()))) {
//                    this.v = out;
//                    return true;
//                }
//                return false;
//            }
//        };
//        //查找注释
//        if (isAnnotation(this, out)) {
//            //找到注释返回
//            return out.getOut();
//        }
        return null;
    }


    /**
     * 连接注释执行链
     * 在当前对象添加执行注释类型
     *
     * @param key 执行注释类型
     * @return 返回本对象
     */
    @Override
    public V join(K key) {
        Assert.notNull(key, "join is key null error");
//        this.add(key);
        return (V) this;
    }

    @Override
    public boolean add(V v) {
        return depend.add(v);
    }

    @Override
    public boolean remove(Object o) {
        return depend.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return depend.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends V> c) {
        return depend.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return depend.removeAll(c);
    }

    @Override
    public boolean removeIf(Predicate<? super V> filter) {
        return depend.removeIf(filter);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return depend.retainAll(c);
    }

    @Override
    public void clear() {
        depend.clear();
    }

    @Override
    public int size() {
        return depend.size();
    }

    @Override
    public boolean isEmpty() {
        return depend.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return depend.contains(o);
    }

    @Override
    public Iterator<V> iterator() {
        return depend.iterator();
    }

    @Override
    public void forEach(Consumer<? super V> action) {
        depend.forEach(action);
    }

    @Override
    public Object[] toArray() {
        return depend.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return depend.toArray(a);
    }

    @Override
    public String toString() {
        return "AnnotationExecutionChain{" +
                "depend=" + depend.size() +
                ", single=" + single +
                ", tag=" + tag.name() +
                ", annotationClass=" + (annotationClass == null ? "" : annotationClass.toString()) +
                ", domain=" + (domain == null ? "" : domain.toString()) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotationExecutionChain<?, ?, ?> that = (AnnotationExecutionChain<?, ?, ?>) o;
        return single == that.single &&
                tag == that.tag &&
                annotationClass.equals(that.annotationClass) &&
                domain.equals(that.domain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(single, tag, annotationClass, domain);
    }
}