package ghost.framework.data.core;//package ghost.framework.data.core;
//
//import org.hibernate.proxy.HibernateProxy;
//import org.hibernate.proxy.LazyInitializer;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:Hibernate工具。
// * @Date: 1:02 2018-06-17
// */
//public final class DataHibernateUtil {
//    /**
//     * 获取代理值。
//     * @param obj
//     * @param <T>
//     * @return
//     */
//    public static <T>  T deproxy (T obj) {
//        if (obj == null)
//            return obj;
//        //判断是否为代理。
//        if (obj instanceof HibernateProxy) {
//            // Unwrap Proxy;
//            //      -- loading, if necessary.
//            HibernateProxy proxy = (HibernateProxy) obj;
//            LazyInitializer li = proxy.getHibernateLazyInitializer();
//            return (T)  li.getImplementation();
//        }
//        return obj;
//    }
//    /**
//     * 是否使用代理。
//     * @param obj
//     * @return
//     */
//    public static boolean isProxy (Object obj) {
//        if (obj instanceof HibernateProxy)
//            return true;
//        return false;
//    }
//    public static boolean isEqual (Object o1, Object o2) {
//        if (o1 == o2)
//            return true;
//        if (o1 == null || o2 == null)
//            return false;
//        Object d1 = deproxy(o1);
//        Object d2 = deproxy(o2);
//        if (d1 == d2 || d1.equals(d2))
//            return true;
//        return false;
//    }
//
//    public static boolean notEqual (Object o1, Object o2) {
//        return ! isEqual( o1, o2);
//    }
//    public static boolean isSame (Object o1, Object o2) {
//        if (o1 == o2)
//            return true;
//        if (o1 == null || o2 == null)
//            return false;
//        Object d1 = deproxy(o1);
//        Object d2 = deproxy(o2);
//        if (d1 == d2)
//            return true;
//        return false;
//    }
//
//    public static boolean notSame (Object o1, Object o2) {
//        return ! isSame( o1, o2);
//    }
//    public static Class getClassWithoutInitializingProxy (Object obj) {
//        if (obj instanceof HibernateProxy) {
//            HibernateProxy proxy = (HibernateProxy) obj;
//            LazyInitializer li = proxy.getHibernateLazyInitializer();
//            return li.getPersistentClass();
//        }
//        // Not a Proxy.
//        return obj.getClass();
//    }
//}
