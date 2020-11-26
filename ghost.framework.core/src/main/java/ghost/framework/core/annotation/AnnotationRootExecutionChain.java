package ghost.framework.core.annotation;

import ghost.framework.beans.annotation.order.Order;
import ghost.framework.beans.annotation.stereotype.Service;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.context.annotation.*;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.proxy.ProxyUtil;
import ghost.framework.util.Assert;
import org.apache.log4j.Logger;

import java.lang.annotation.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * package: ghost.framework.core.annotation
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注释根执行链
 * @Date: 2020/2/17:0:40
 * @param <K> 注释类型
 * @param <V> 注释链对象
 * @param <L> 注释链依赖列表
 */
public class AnnotationRootExecutionChain
        <
                K extends Class<? extends Annotation>,
                V extends IAnnotationExecutionChain<K, V, L>,
                L extends List<V>
                >
        extends AbstractMap<V, L>
        implements IAnnotationRootExecutionChain<K, V, L> {
    public AnnotationRootExecutionChain(IApplication application) {
        app = application;
        this.exclusionList.add((K) Documented.class);
        this.exclusionList.add((K) Target.class);
        this.exclusionList.add((K) Inherited.class);
        this.exclusionList.add((K) Retention.class);
        this.exclusionList.add((K) Repeatable.class);
        //
        packageEnumSort = Arrays.asList(
                new AnnotationEnumTagSort[]{
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Container),
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.StereoType),
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Package),
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Conditional),
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Locale),
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Configuration)
                });
        classEnumSort = Arrays.asList(
                new AnnotationEnumTagSort[]{
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Container),
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.StereoType),
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Package),
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Conditional),
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Locale),
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Configuration)
                });
        constructorEnumSort = Arrays.asList(
                new AnnotationEnumTagSort[]{
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Container),
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Conditional),
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Locale),
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Configuration)
                });
        parameterEnumSort = Arrays.asList(
                new AnnotationEnumTagSort[]{
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Container),
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Injection),
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Conditional),
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Locale),
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Configuration)
                });
        methodEnumSort = Arrays.asList(
                new AnnotationEnumTagSort[]{
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Container),
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Bean),
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Invoke),
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Conditional),
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Locale),
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Configuration)
                });
        fieldEnumSort = Arrays.asList(
                new AnnotationEnumTagSort[]{
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Container),
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Injection),
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Conditional),
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Locale),
                        new AnnotationEnumTagSort(app, AnnotationTag.AnnotationTags.Configuration)
                });
        //排序依赖执行顺序反转排序
        Collections.reverse(classEnumSort);
        Collections.reverse(methodEnumSort);
        Collections.reverse(parameterEnumSort);
        Collections.reverse(fieldEnumSort);
        Collections.reverse(packageEnumSort);
    }

    private List<AnnotationEnumTagSort> packageEnumSort;
    /**
     * 枚举排序
     */
    private List<AnnotationEnumTagSort> classEnumSort;
    private List<AnnotationEnumTagSort> constructorEnumSort;
    private List<AnnotationEnumTagSort> parameterEnumSort;
    private List<AnnotationEnumTagSort> methodEnumSort;
    private List<AnnotationEnumTagSort> fieldEnumSort;
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(AnnotationRootExecutionChain.class);
    /**
     * 排除注释列表
     */
    private List<K> exclusionList = new ArrayList<K>();

    /**
     * 删除排除注释
     *
     * @param k 排除注释
     * @return
     */
    @Override
    public boolean removeExclusion(K k) {
        synchronized (exclusionList) {
            return exclusionList.remove(k);
        }
    }

    /**
     * 添加排除注释
     *
     * @param k 排除注释
     * @return
     */
    @Override
    public boolean addExclusion(K k) {
        synchronized (exclusionList) {
            if (exclusionList.contains(k)) {
                return false;
            }
            return exclusionList.add(k);
        }
    }

    /**
     * @return
     */
    @Override
    public IApplication getApp() {
        return app;
    }

    private static IApplication app;
    /**
     * 注释执行链地图
     */
    private Map<V, L> mapSet = new HashMap();

    @Override
    public Set<Entry<V, L>> entrySet() {
        return mapSet.entrySet();
    }

    @Override
    public L put(V key, L value) {
        mapSet.put(key, value);
        return value;
    }

    /**
     * 重写比对 {@link K} 注释类型
     *
     * @param key {@link K} 注释类型
     * @return 返回 {@link K} 注释类型是否存在
     */
    @Override
    public boolean containsKey(Object key) {
        Assert.notNull(key, "containsKey is key null error");
        return mapSet.entrySet().stream().anyMatch(a -> a.getKey().getAnnotationClass().equals(key));
    }

    /**
     * 获取注释的对象
     *
     * @param key 注释值
     * @return
     */
    public V get(K key) {
        Assert.notNull(key, "get is key null error");
        IOutAnnotation<K, V, L> outAnnotation = new OutAnnotation<K, V, L>(null, key) {
            private V v;

            @Override
            public V getOut() {
                return v;
            }

            @Override
            public boolean test(V out) {
                if (out.getAnnotationClass().equals(this.getKey())) {
                    this.v = out;
                    return true;
                }
                return false;
            }
        };
        //等待返回是否找到对象
        if (mapSet.entrySet().stream().anyMatch(a -> outAnnotation.test(a.getKey()))) {
            return outAnnotation.getOut();
        }
        return null;
    }

    /**
     * 添加依赖注释
     *
     * @param key
     * @param keys
     * @param tag
     * @return
     */
    @Override
    public V addDepend(K key, K[] keys, AnnotationTag.AnnotationTags tag) {
        Assert.notNull(key, "addDepend is key null error");
        Assert.isTrue(keys == null && keys.length == 0, "addDepend is keys null error");
        Assert.notNull(tag, "addDepend is tag null error");
        V v = this.get(key);
        for (K k : keys) {
            V kv = this.add(k, tag);
            if (kv == null || v.getDepend().contains(kv)) {
                this.logger.warn("addDepend in " + (key.toString().replace("interface ", "annotation ") + " is exist " + k.toString().replace("interface ", "annotation ")));
                continue;
            }
            v.getDepend().add(kv);
        }
        return v;
    }

    /**
     * 添加依赖注释
     *
     * @param key
     * @param keys
     * @param tag
     * @return
     */
    @Override
    public V addDepend(K key, K[] keys, AnnotationTag.AnnotationTags tag, Object domain) {
        Assert.notNull(key, "addDepend is key null error");
        Assert.isTrue(keys == null && keys.length == 0, "addDepend is keys null error");
        Assert.notNull(tag, "addDepend is tag null error");
        Assert.notNull(domain, "addDepend is domain null error");
        V v = this.get(key);
        for (K k : keys) {
            V kv = this.add(k, tag, domain);
            if (kv == null || v.getDepend().contains(kv)) {
                this.logger.warn("addDepend in " + (key.toString().replace("interface ", "annotation ") + " is " + (kv == null ? "null" : ("exist " + k.toString().replace("interface ", "annotation ")))));
                continue;
            }
            v.getDepend().add(kv);
        }
        return v;
    }

    /**
     * 添加依赖注释
     *
     * @param key
     * @param keys
     * @param tag
     * @param domain
     * @return
     */
    @Override
    public V[] addDepend(K[] key, K[] keys, AnnotationTag.AnnotationTags tag, Object domain) {
        Assert.isTrue(key == null && key.length == 0, "addDepend is key null error");
        Assert.isTrue(keys == null && keys.length == 0, "addDepend is keys null error");
        Assert.notNull(tag, "addDepend is tag null error");
        Assert.notNull(domain, "addDepend is domain null error");
        V[] vs = (V[]) new AnnotationExecutionChain[key.length];
        for (int i = 0; i < key.length; i++) {
            vs[i] = this.get(key[i]);
            for (K k : keys) {
                V kv = this.add(k, tag, domain);
                if (kv == null || vs[i].getDepend().contains(kv)) {
                    this.logger.warn("addDepend in " + (key[i].toString().replace("interface ", "annotation ") + " is " + (kv == null ? "null" : ("exist " + k.toString().replace("interface ", "annotation ")))));
                    continue;
                }
                vs[i].getDepend().add(kv);
            }
        }
        return vs;
    }

    /**
     * 添加依赖注释
     *
     * @param key
     * @param keys
     * @param tag
     * @return
     */
    @Override
    public V[] addDepend(K[] key, K[] keys, AnnotationTag.AnnotationTags tag) {
        Assert.isTrue(key == null && key.length == 0, "addDepend is key null error");
        Assert.isTrue(keys == null && keys.length == 0, "addDepend is keys null error");
        Assert.notNull(tag, "addDepend is tag null error");
        V[] vs = (V[]) new AnnotationExecutionChain[key.length];
        for (int i = 0; i < key.length; i++) {
            vs[i] = this.get(key[i]);
            for (K k : keys) {
                V kv = this.add(k, tag);
                if (kv == null || vs[i].getDepend().contains(kv)) {
                    this.logger.warn("addDepend in " + (key[i].toString().replace("interface ", "annotation ") + " is " + (kv == null ? "null" : ("exist " + k.toString().replace("interface ", "annotation ")))));
                    continue;
                }
                vs[i].getDepend().add(kv);
            }
        }
        return vs;
    }

    /**
     * 添加依赖注释
     *
     * @param key
     * @param keys
     * @return
     */
    @Override
    public V addDepend(K key, K[] keys) {
        Assert.notNull(key, "addDepend is key null error");
        Assert.isTrue(keys == null && keys.length == 0, "addDepend is keys null error");
        V v = this.get(key);
        for (K k : keys) {
            V kv = this.add(k);
            if (kv == null || v.getDepend().contains(kv)) {
                this.logger.warn("addDepend in " + (key.toString().replace("interface ", "annotation ") + " is " + (kv == null ? "null" : ("exist " + k.toString().replace("interface ", "annotation ")))));
                continue;
            }
            v.getDepend().add(kv);
        }
        return v;
    }

    /**
     * 添加依赖注释
     *
     * @param key
     * @param keys
     * @return
     */
    @Override
    public V[] addDepend(K[] key, K[] keys) {
        Assert.isTrue(key == null && key.length == 0, "addDepend is key null error");
        Assert.isTrue(keys == null && keys.length == 0, "addDepend is keys null error");
        V[] vs = (V[]) new AnnotationExecutionChain[key.length];
        for (int i = 0; i < key.length; i++) {
            vs[i] = this.get(key[i]);
            for (K k : keys) {
                V kv = this.add(k);
                if (kv == null || vs[i].getDepend().contains(kv)) {
                    this.logger.warn("addDepend in " + (key[i].toString().replace("interface ", "annotation ") + " is " + (kv == null ? "null" : ("exist " + k.toString().replace("interface ", "annotation ")))));
                    continue;
                }
                vs[i].getDepend().add(kv);
            }
        }
        return vs;
    }

    /**
     * 添加默认域注释
     *
     * @param keys 注释类型
     * @return 返回添加创建对象
     */
    @Override
    public V[] add(K[] keys) {
        Assert.isTrue(keys == null && keys.length == 0, "add is keys null error");
        V[] vs = (V[]) new AnnotationExecutionChain[keys.length];
        for (int i = 0; i < keys.length; i++) {
            vs[i] = this.add(keys[i]);
        }
        return vs;
    }

    /**
     * 添加默认域注释
     *
     * @param keys 注释类型
     * @param tag
     * @return 返回添加创建对象
     */
    @Override
    public V[] add(K[] keys, AnnotationTag.AnnotationTags tag) {
        Assert.isTrue(keys == null && keys.length == 0, "add is keys null error");
        Assert.notNull(tag, "add is tag null error");
        V[] vs = (V[]) new AnnotationExecutionChain[keys.length];
        for (int i = 0; i < keys.length; i++) {
            vs[i] = this.add(keys[i], tag);
        }
        return vs;
    }

    /**
     * 添加默认域注释
     *
     * @param keys   注释类型
     * @param tag
     * @param domain
     * @return 返回添加创建对象
     */
    @Override
    public V[] add(K[] keys, AnnotationTag.AnnotationTags tag, Object domain) {
        Assert.isTrue(keys == null && keys.length == 0, "add is keys null error");
        Assert.notNull(tag, "add is tag null error");
        Assert.notNull(domain, "add is domain null error");
        V[] vs = (V[]) new AnnotationExecutionChain[keys.length];
        for (int i = 0; i < keys.length; i++) {
            vs[i] = this.add(keys[i], tag, domain);
        }
        return vs;
    }

    /**
     * 添加默认域注释
     *
     * @param key 注释类型
     * @return 返回添加创建对象
     */
    @Override
    public V add(K key) {
        Assert.notNull(key, "add is key null error");
        synchronized (this.exclusionList) {
            if (this.exclusionList.contains(key)) {
                this.logger.warn("add in " + key.toString().replace("interface ", "annotation ") + " is exclusion");
                return null;
            }
            synchronized (mapSet) {
                if (this.containsKey(key)) {
                    this.logger.warn("add in " + key.toString().replace("interface ", "annotation ") + " is exist");
                    return this.get(key);
                }
                V m = (V) new AnnotationExecutionChain<K, V, L>(key, app);
                this.put(m, m.getDepend());
                //遍历注释依赖处理
                for (Annotation annotation : key.getAnnotations()) {
                    K ak = (K) ProxyUtil.getProxyObjectAnnotationClass(annotation);
                    if (this.exclusionList.contains(ak)) {
                        this.logger.warn("add in " + ak.toString().replace("interface ", "annotation ") + " is exclusion");
                        return null;
                    }
                    if (this.containsKey(ak)) {
                        System.out.println(ak.toString());
                        m.add(this.get(ak));
                    } else {
                        V c = (V) new AnnotationExecutionChain<K, V, L>(ak, app);
                        this.put(c, c.getDepend());
                        m.add(c);
                        this.add(ak);
                    }
                }
                return m;
            }
        }
    }

    /**
     * 添加默认域注释
     *
     * @param key 注释类型
     * @param tag 注释标签
     * @return 返回添加创建对象
     */
    @Override
    public V add(K key, AnnotationTag.AnnotationTags tag) {
        Assert.notNull(key, "add is key null error");
        Assert.notNull(tag, "add is tag null error");
        synchronized (this.exclusionList) {
            if (this.exclusionList.contains(key)) {
                this.logger.warn("add in " + key.toString().replace("interface ", "annotation ") + " is exclusion");
                return null;
            }
            synchronized (mapSet) {
                if (this.containsKey(key)) {
                    this.logger.warn("add in " + key.toString().replace("interface ", "annotation ") + " is exist");
                    return this.get(key);
                }
                V m = (V) new AnnotationExecutionChain<K, V, L>(key, tag, app);
                this.put(m, m.getDepend());
                //遍历注释依赖处理
                for (Annotation annotation : key.getAnnotations()) {
                    K ak = (K) ProxyUtil.getProxyObjectAnnotationClass(annotation);
                    if (this.exclusionList.contains(ak)) {
                        this.logger.warn("add in " + ak.toString().replace("interface ", "annotation ") + " is exclusion");
                        return null;
                    }
                    if (this.containsKey(ak)) {
                        System.out.println(ak.toString());
                        m.add(this.get(ak));
                    } else {
                        V c = (V) new AnnotationExecutionChain<K, V, L>(ak, app);
                        this.put(c, c.getDepend());
                        m.add(c);
                        this.add(ak);
                    }
                }
                return m;
            }
        }
    }
    /**
     * 添加指定域注释
     *
     * @param key    注释类型
     * @param single 该类型注释是否为单实例
     * @param tag    注释标签
     * @param domain 注释域
     * @return 返回添加创建对象
     */
    @Override
    public V add(K key, boolean single, AnnotationTag.AnnotationTags tag, Object domain) {
        Assert.notNull(key, "add is key null error");
        Assert.notNull(tag, "add is tag null error");
        Assert.notNull(domain, "add is domain null error");
        synchronized (this.exclusionList) {
            if (this.exclusionList.contains(key)) {
                this.logger.warn("add in " + key.toString().replace("interface ", "annotation ") + " is exclusion");
                return null;
            }
            synchronized (mapSet) {
                if (this.containsKey(key)) {
                    this.logger.warn("add in " + key.toString().replace("interface ", "annotation ") + " is exist");
                    return this.get(key);
                }
                V m = (V) new AnnotationExecutionChain<K, V, L>(key, single, tag, domain);
                this.put(m, m.getDepend());
                //遍历注释依赖处理
                for (Annotation annotation : key.getAnnotations()) {
                    K ak = (K) ProxyUtil.getProxyObjectAnnotationClass(annotation);
                    if (this.exclusionList.contains(ak)) {
                        this.logger.warn("add in " + ak.toString().replace("interface ", "annotation ") + " is exclusion");
                        return null;
                    }
                    if (this.containsKey(ak)) {
                        System.out.println(ak.toString());
                        m.add(this.get(ak));
                    } else {
                        V c = (V) new AnnotationExecutionChain<K, V, L>(ak, domain);
                        this.put(c, c.getDepend());
                        m.add(c);
                        this.add(ak);
                    }
                }
                return m;
            }
        }
    }
    /**
     * 添加指定域注释
     *
     * @param key    注释类型
     * @param tag    注释标签
     * @param domain 注释域
     * @return 返回添加创建对象
     */
    @Override
    public V add(K key, AnnotationTag.AnnotationTags tag, Object domain) {
        return this.add(key, false, tag, domain);
    }

    /**
     * 获取注释链
     *
     * @param target
     * @return
     */
    @Override
    public Map<K, ExecutionAnnotation<K, V, L>> getExecutionChain(Constructor target) {
        //声明返回注释链地图
        Map<K, ExecutionAnnotation<K, V, L>> list = new HashMap<>();
        //声明注释链列表
        List<V> vList = this.getExecutionChainList(target.getDeclaredAnnotations());
        //判断是否需要排列注释链
        if (vList.size() <= 1) {
            for (V v : vList) {
                list.put(v.getAnnotationClass(), new ExecutionAnnotation<>(v));
            }
            return list;
        }
        return this.sort(vList, constructorEnumSort);
    }

    /**
     * 获取注释链
     *
     * @param target
     * @return
     */
    @Override
    public Map<K, ExecutionAnnotation<K, V, L>> getExecutionChain(Parameter target) {
        //声明返回注释链地图
        Map<K, ExecutionAnnotation<K, V, L>> list = new HashMap<>();
        //声明注释链列表
        List<V> vList = this.getExecutionChainList(target.getAnnotations());
        //判断是否需要排列注释链
        if (vList.size() <= 1) {
            for (V v : vList) {
                list.put(v.getAnnotationClass(), new ExecutionAnnotation<>(v));
            }
            return list;
        }
        return this.sort(vList, parameterEnumSort);
    }

    /**
     * 获取注释链
     *
     * @param target
     * @return
     */
    @Override
    public Map<K, ExecutionAnnotation<K, V, L>> getExecutionChain(Package target) {
        //声明返回注释链地图
        Map<K, ExecutionAnnotation<K, V, L>> list = new HashMap<>();
        //声明注释链列表
        List<V> vList = this.getExecutionChainList(target.getAnnotations());
        //判断是否需要排列注释链
        if (vList.size() <= 1) {
            for (V v : vList) {
                list.put(v.getAnnotationClass(), new ExecutionAnnotation<>(v));
            }
            return list;
        }
        return this.sort(vList, packageEnumSort);
    }

    /**
     * 获取注释链
     *
     * @param target
     * @return
     */
    @Override
    public Map<K, ExecutionAnnotation<K, V, L>> getExecutionChain(Method target) {
        //声明返回注释链地图
        Map<K, ExecutionAnnotation<K, V, L>> list = new HashMap<>();
        //声明注释链列表
        List<V> vList = this.getExecutionChainList(target.getDeclaredAnnotations());
        //判断是否需要排列注释链
        if (vList.size() <= 1) {
            for (V v : vList) {
                list.put(v.getAnnotationClass(), new ExecutionAnnotation<>(v));
            }
            return list;
        }
        return this.sort(vList, methodEnumSort);
    }

    /**
     * 获取注释链
     *
     * @param target
     * @return
     */
    @Override
    public Map<K, ExecutionAnnotation<K, V, L>> getExecutionChain(Field target) {
        //声明返回注释链地图
        Map<K, ExecutionAnnotation<K, V, L>> list = new HashMap<>();
        //声明注释链列表
        List<V> vList = this.getExecutionChainList(target.getDeclaredAnnotations());
        //判断是否需要排列注释链
        if (vList.size() <= 1) {
            for (V v : vList) {
                list.put(v.getAnnotationClass(), new ExecutionAnnotation<>(v));
            }
            return list;
        }
        return this.sort(vList, fieldEnumSort);
    }

    /**
     * 获取注释链
     *
     * @param target
     * @return
     */
    @Override
    public Map<K, ExecutionAnnotation<K, V, L>> getExecutionChain(Class<?> target) {
        //声明返回注释链地图
        Map<K, ExecutionAnnotation<K, V, L>> list = new HashMap<>();
        //声明注释链列表
        List<V> vList = this.getExecutionChainList(target.getDeclaredAnnotations());
        //判断是否需要排列注释链
        if (vList.size() <= 1) {
            for (V v : vList) {
                list.put(v.getAnnotationClass(), new ExecutionAnnotation<>(v));
            }
            return list;
        }
        return this.sort(vList, classEnumSort);
    }

    /**
     * 获取注释链
     *
     * @param annotations 素组注释，可以是类型，可以是函数，可以是参数，可以是构建，可以是声明，可以是包等的数组注释
     * @return
     */
    @Override
    public List<V> getExecutionChainList(Annotation[] annotations) {
        //声明注释链列表
        List<V> vList = new ArrayList<>();
        //判断是否有注释
        if (annotations.length == 0) {
            //没有注释直接返回
            return vList;
        }
        //获取全部注释依赖列表
        List<K> kList = new ArrayList<>();
        for (Annotation annotation : annotations) {
            this.fill(kList, (K) ProxyUtil.getProxyObjectAnnotationClass(annotation));
        }
        //清理没有在注释链的注释
        List<K> del = new ArrayList<>();
        kList.forEach(new Consumer<K>() {
            @Override
            public void accept(K k) {
                if (!containsKey(k)) {
                    del.add(k);
                }
            }
        });
        kList.removeAll(del);
        kList.forEach(new Consumer<K>() {
            @Override
            public void accept(K k) {
                vList.add(get(k));
            }
        });
        return vList;
    }

    /**
     * 获取注释链
     *
     * @param annotations 素组注释，可以是类型，可以是函数，可以是参数，可以是构建，可以是声明，可以是包等的数组注释
     * @return
     */
    @Override
    public Map<K, ExecutionAnnotation<K, V, L>> getExecutionChain(Annotation[] annotations) {
        //声明返回注释链地图
        Map<K, ExecutionAnnotation<K, V, L>> list = new HashMap<>();
        //判断是否有注释
        if (annotations.length == 0) {
            //没有注释直接返回
            return list;
        }
        //声明注释链列表
        List<V> vList = this.getExecutionChainList(annotations);
        //判断是否需要排列注释链
        if (vList.size() <= 1) {
            for (V v : vList) {
                list.put(v.getAnnotationClass(), new ExecutionAnnotation<>(v));
            }
            return list;
        }
        return this.sort(vList, classEnumSort);
    }

    private void fillSort(List<V> root, Set<V> set) {
        for (V v : root) {
            if (set.addAll(v.getDepend())) {
                this.fillSort(v.getDepend(), set);
            }
        }
    }

    /**
     * 排序
     *
     * @param root
     * @param enumSort 排序枚举
     */
    private LinkedHashMap<K, ExecutionAnnotation<K, V, L>> sort(List<V> root, List<AnnotationEnumTagSort> enumSort) {
        //按照枚举排序
        List<V> newRoot = new ArrayList<>();
        for (AnnotationEnumTagSort s : enumSort) {
            for (V v : root) {
                if (s.getName().equals(v.getTag().name())) {
                    newRoot.add(v);
                }
            }
        }
        //按照依赖深度数据量排序，小为先
        newRoot.sort(new Comparator<V>() {
            @Override
            public int compare(V o1, V o2) {
                //两个依赖同等判断排序注释计算排序位置
                if (o2.getTag().equals(o1.getTag()) && o1.getAnnotationClass().isAnnotationPresent(Order.class) && o2.getAnnotationClass().isAnnotationPresent(Order.class)) {
                    return o1.getAnnotationClass().getAnnotation(Order.class).value() - o2.getAnnotationClass().getAnnotation(Order.class).value();
                }
                //返回0不做排序处理
                return 0;
            }
        });
        //填充返回注释执行链
        LinkedHashMap<K, ExecutionAnnotation<K, V, L>> map = new LinkedHashMap<K, ExecutionAnnotation<K, V, L>>();
        newRoot.forEach(new Consumer<V>() {
            @Override
            public void accept(V entry) {
                map.put(entry.getAnnotationClass(), new ExecutionAnnotation<K, V, L>(entry));
            }
        });
        //调试测试
        if (logger.isDebugEnabled()) {
            map.entrySet().stream().anyMatch(new Predicate<Entry<K, ExecutionAnnotation<K, V, L>>>() {
                @Override
                public boolean test(Entry<K, ExecutionAnnotation<K, V, L>> entry) {
                    if (entry.getKey().equals(Service.class)) {
                        return true;
                    }
                    return false;
                }
            });
        }
        return map;
    }

    private void fill(List<K> list, K k) {
        if (list.contains(k)) {
            return;
        }
        list.add(k);
        for (Annotation annotation : k.getDeclaredAnnotations()) {
            K ck = (K) ProxyUtil.getProxyObjectAnnotationClass(annotation);
            if (list.contains(ck)) {
                continue;
            }
            this.fill(list, ck);
        }
    }

    /**
     * 删除注释
     *
     * @param key 注释类型
     * @return
     */
    @Override
    public V remove(K key) {
        synchronized (mapSet) {
            V v = this.get(key);
            if (v == null) {
                return null;
            }
            //
            this.remove(v);
            //删除依赖域
            mapSet.entrySet().forEach(new Consumer<Entry<V, L>>() {
                @Override
                public void accept(Entry<V, L> vlEntry) {
                    List<V> vList = new ArrayList<>();
                    vlEntry.getValue().forEach(new Consumer<V>() {
                        @Override
                        public void accept(V v) {
                            if (v.getAnnotationClass().equals(key)) {
                                vList.add(v);
                            }
                        }
                    });
                    vlEntry.getValue().removeAll(vList);
                }
            });
            //
            return v;
        }
    }

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
        //查找注释
        synchronized (mapSet) {
            for (Map.Entry<V, L> entry : mapSet.entrySet()) {
                if (entry.getKey().getDomain().equals(domain)) {
                    list.add(entry.getKey());
                }
            }
        }
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
        synchronized (mapSet) {
            //删除依赖域
            mapSet.entrySet().forEach(new Consumer<Entry<V, L>>() {
                @Override
                public void accept(Entry<V, L> vlEntry) {
                    List<V> vList = new ArrayList<>();
                    vlEntry.getValue().forEach(new Consumer<V>() {
                        @Override
                        public void accept(V v) {
                            if (v.getDomain().equals(domain)) {
                                vList.add(v);
                            }
                        }
                    });
                    vlEntry.getValue().removeAll(vList);
                }
            });
            //删除垂直域
            list.forEach(mapSet::remove);
        }
        //判断是否有删除域
        return list.size() > 0;
    }
}