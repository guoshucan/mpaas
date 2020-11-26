//package ghost.framework.core.application.event;
//
//import ghost.framework.beans.annotation.event.BeanApplicationEventListener;
//import ghost.framework.beans.annotation.event.BeanEventListenerProcessor;
//import ghost.framework.beans.annotation.stereotype.Component;
//import ghost.framework.beans.application.event.AbstractApplicationEvent;
//import ghost.framework.context.application.event.ApplicationEventListenerSource;
//import ghost.framework.context.application.event.ApplicationEventListener;
//import ghost.framework.context.application.event.IEventListenerProcessorContainer;
//import org.apache.log4j.Logger;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//
///**
// * package: ghost.framework.core.application.event
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:事件监听处理器容器
// * @Date: 2020/2/16:12:01
// */
//@Component
//@BeanEventListenerProcessor
//public class EventListenerProcessorContainer
//        <
//                E extends AbstractApplicationEvent,
//                L extends ApplicationEventListener<E>
//                >
//        implements IEventListenerProcessorContainer<E, L> {
//     private Log log = LogFactory.getLog(EventListenerProcessorContainer.class);
//    /**
//     * 监听源列表
//     */
//    private Map<String,  ApplicationEventListenerSource> sourceMap = new HashMap<>();
//    /**
//     * 删除监听源
//     *
//     * @param source 监听源
//     * @return
//     */
//    @Override
//    public void removeSource(L source) {
//        BeanApplicationEventListener sourceEventListener = source.getClass().getAnnotation(BeanApplicationEventListener.class);
////        K k = (K) new ApplicationEventListenerSource(source, sourceEventListener.eventType(), sourceEventListener.topic());
////        synchronized (sourceMap) {
////            sourceMap.remove(k);
////        }
////        synchronized (sourceMap) {
////            sourceEventListenerMap.entries().forEach(new Consumer<Map.Entry<K, Multimap<MK, V>>>() {
////                @Override
////                public void accept(Map.Entry<K, Multimap<MK, V>> entry) {
////                    if (log.isDebugEnabled()) {
////                        Multimap<MK, V> methodEventListenerMap = (Multimap<MK, V>) sourceMap.get(entry.getKey());
////                        log.debug("removeSource:" + source.toString());
////                        methodEventListenerMap.entries().forEach(new Consumer<Map.Entry<MK, V>>() {
////                            @Override
////                            public void accept(Map.Entry<MK, V> mkvEntry) {
////                                log.debug("removeSource:" + entry.getKey().toString() + ">method:" + mkvEntry.getValue().toString());
////                            }
////                        });
////                    } else {
////                        sourceMap.remove(entry.getKey());
////                    }
////                }
////            });
////        }
//    }
//
//    /**
//     * 添加监听源
//     *
//     * @param source 监听源
//     */
//    @Override
//    public void addSource(L source) {
////        MultiValuedMap<K, Multimap<MK, V>> sourceEventListenerMap = new ArrayListValuedHashMap();
////        Multimap<MK, V> methodEventListenerMap = TreeMultimap.create();
////        //应用事件监听源注释
////        BeanApplicationEventListener sourceEventListener = source.getClass().getAnnotation(BeanApplicationEventListener.class);
////        if (sourceEventListener == null) {
////            throw new ApplicationEventListenerException(source.getClass().getName());
////        }
////        //获取源应用事件监听函数列表
////        List<Method> methodList = ReflectUtil.getAllAnnotationMethods(source, BeanMethodEventListener.class);
////        //没有监听函数错误
////        if (methodList.size() == 0) {
////            throw new ApplicationEventListenerNotMethodException(source.getClass().getName());
////        }
////        ApplicationEventListenerSource aels = new ApplicationEventListenerSource(source, sourceEventListener.eventType(), sourceEventListener.topic());
////        if (sourceEventListener.topic().length == 0) {
////            synchronized (sourceMap) {
////                Map<ApplicationEventType, ApplicationEventListenerSource> map = sourceMap.get(sourceEventListener.eventType());
////                if (map == null) {
////                    Map<ApplicationEventType, ApplicationEventListenerSource> typeMap = new HashMap<>();
////                    sourceMap.put("", typeMap);
////                }
////            }
////        } else {
////            for (String s : sourceEventListener.topic()) {
////
////            }
////        }
////        //创建监听源对象
////        sourceEventListenerMap.put((K) aels, methodEventListenerMap);
////        //监听函数排序
////        OrderAnnotationUtil.methodListSort(methodList);
////        //遍历监听函数列表
////        for (Method method : methodList) {
////            V methodEventListener = (V) new MethodEventListenerSource(aels, method);
////            if (methodEventListener.getTopic().length == 0) {
////                //没有指定主题
////                methodEventListenerMap.put((MK) "", methodEventListener);
////            } else {
////                //指定主题
////                for (String s : methodEventListener.getTopic()) {
////                    methodEventListenerMap.put((MK) s, methodEventListener);
////                }
////            }
////        }
////        //合并
////        synchronized (this.sourceMap) {
////            sourceEventListenerMap.entries().forEach(new Consumer<Map.Entry<K, Multimap<MK, V>>>() {
////                @Override
////                public void accept(Map.Entry<K, Multimap<MK, V>> entry) {
////                    sourceMap.put(entry.getKey(), entry.getValue());
////                    if (log.isDebugEnabled()) {
////                        log.debug("addSource:" + entry.getKey().toString());
////                        entry.getValue().entries().forEach(new Consumer<Map.Entry<MK, V>>() {
////                            @Override
////                            public void accept(Map.Entry<MK, V> mkvEntry) {
////                                log.debug("addSource:" + entry.getKey().toString() + ">method:" + mkvEntry.getValue().toString());
////                            }
////                        });
////                    }
////                }
////            });
////        }
//    }
//
//    /**
//     * 重写接收事件处理
//     *
//     * @param event
//     */
//    @Override
//    public void onApplicationEvent(E event) {
////        synchronized (sourceMap) {
////            sourceMap.entries().stream().anyMatch(new Predicate<Map.Entry<K, Multimap<MK, V>>>() {
////                @Override
////                public boolean test(Map.Entry<K, Multimap<MK, V>> entry) {
////                    //比对源主题，这里判断默认空字符串主题，表示未指定任何主题
//////                    while (entry.getKey().getTopic().equals("")) {
////                    //源已经处理，立即返回，不在延续
////                    return entry.getValue().entries().stream().anyMatch(new Predicate<Map.Entry<MK, V>>() {
////                        @Override
////                        public boolean test(Map.Entry<MK, V> mkvEntry) {
////                            //比对监听调用函数主题，这里判断默认空字符串主题，表示未指定任何主题
////                            if (mkvEntry.getKey().equals("")) {
////                                try {
////                                    //调用监听函数
////                                    mkvEntry.getValue().getMethod().invoke(mkvEntry.getValue().getSource(), event);
////                                    //返回状态，如果已经处理将不再传递
////                                    return event.isHandle();
////                                } catch (Exception e) {
////                                    if (log.isDebugEnabled()) {
////                                        e.printStackTrace();
////                                        log.debug(e.getMessage(), e);
////                                    } else {
////                                        log.error(e.getMessage(), e);
////                                    }
////                                }
////                            }
////                            //处理监听函数有指定主题
////
////                            return false;
////                        }
////                    });
//////                    }
////                    //处理监听源有指定主题
//////                    return false;
////                }
////            });
////        }
//    }
//
////    @Override
////    public boolean equals(Object o) {
////        if (this == o) return true;
////        if (o == null || getClass() != o.getClass()) return false;
////        EventListenerProcessorContainer<?, ?, ?, ?, ?> that = (EventListenerProcessorContainer<?, ?, ?, ?, ?>) o;
////        return sourceMap.equals(that.sourceMap);
////    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(sourceMap);
//    }
//
//    @Override
//    public String toString() {
//        return "EventListenerProcessorContainer{" +
//                "sourceMap=" + sourceMap.toString() +
//                '}';
//    }
//}