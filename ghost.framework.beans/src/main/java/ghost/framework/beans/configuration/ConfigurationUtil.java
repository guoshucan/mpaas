//package ghost.framework.beans.configuration;
//
//import ghost.framework.beans.annotation.configuration.properties.ConfigurationProperties;
//import ghost.framework.beans.annotation.configuration.properties.ConfigurationPropertiess;
//
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:配置工具类
// * @Date: 9:30 2019-06-02
// */
//public final class ConfigurationUtil {
//    /**
//     * 获取类注释配置列表
//     * 排列对象从小开始到大开始位置列表
//     *
//     * @param c 获取配置注释的类
//     * @return
//     */
//    public static List<ConfigurationProperties> getClassOrderList(Class<?> c) {
//        List<ConfigurationProperties> propertiesList = new ArrayList<>();
//        if (c.isAnnotationPresent(ConfigurationPropertiess.class)) {
//            for (ConfigurationProperties properties : c.getAnnotation(ConfigurationPropertiess.class).value()) {
//                propertiesList.add(properties);
//            }
//        }
//        if (c.isAnnotationPresent(ConfigurationProperties.class)) {
//            for (ConfigurationProperties properties : c.getAnnotationsByType(ConfigurationProperties.class)) {
//                propertiesList.add(properties);
//            }
//        }
//        getOrderList(propertiesList);
//        return propertiesList;
//    }
//
//    /**
//     * 排列对象从小开始到大开始位置列表。
//     *
//     * @param list
//     * @return
//     */
//    public static List<ConfigurationProperties> getOrderList(ConfigurationProperties[] list) {
//        List<ConfigurationProperties> minList = new ArrayList();
//        for (ConfigurationProperties properties : list) {
//            minList.add(properties);
//        }
//        getOrderList(minList);
//        return minList;
//    }
//
//    /**
//     * 排列对象从小开始到大开始位置列表。
//     *
//     * @param list
//     */
//    public static void getOrderList(List<ConfigurationProperties> list) {
//        list.sort(new Comparator<ConfigurationProperties>() {
//            @Override
//            public int compare(ConfigurationProperties a, ConfigurationProperties b) {
//                return a.order() - b.order();
//            }
//        });
//    }
//}