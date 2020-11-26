//package ghost.framework.web.angular1x.context.resource;
//
//import ghost.framework.beans.annotation.stereotype.Component;
//
//import java.util.AbstractMap;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//
///**
// * package: ghost.framework.web.angular1x.context.resource
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:web资源容器
// * @Date: 2020/3/13:18:21
// */
//@Component
//public class WebResourceContainer extends AbstractMap<String, IWebResourceItem> implements IWebResourceContainer {
//    private Map<String, IWebResourceItem> map = new HashMap<>();
//
//    @Override
//    public Set<Entry<String, IWebResourceItem>> entrySet() {
//        return map.entrySet();
//    }
//
//    @Override
//    public IWebResourceItem put(String key, IWebResourceItem value) {
//        return map.put(key, value);
//    }
//}