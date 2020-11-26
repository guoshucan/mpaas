//package ghost.framework.web.angular1x.context.router;
//
//import java.util.AbstractMap;
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.Set;
//
///**
// * package: ghost.framework.web.angular1x.context.router
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 2020/4/25:15:27
// */
//public class WebRouterHeaderMenuGroup extends AbstractMap<String, IWebRouterHeaderMenuTitle> implements IWebRouterHeaderMenuGroup {
//    @Override
//    public String getGroup() {
//        return group;
//    }
//
//    private String group;
//
//    @Override
//    public void setGroup(String group) {
//        this.group = group;
//    }
//
//    @Override
//    public boolean isUse() {
//        return use;
//    }
//
//    @Override
//    public void setUse(boolean use) {
//        this.use = use;
//    }
//
//    private boolean use = true;
//    private Map<String, IWebRouterHeaderMenuTitle> map = new LinkedHashMap<>();
//
//    @Override
//    public Set<Entry<String, IWebRouterHeaderMenuTitle>> entrySet() {
//        return map.entrySet();
//    }
//}