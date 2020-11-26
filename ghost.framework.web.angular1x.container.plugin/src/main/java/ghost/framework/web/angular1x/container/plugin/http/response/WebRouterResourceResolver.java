//package ghost.framework.web.angular1x.container.plugin.http.response;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.module.Module;
//import ghost.framework.beans.annotation.stereotype.Component;
//import ghost.framework.web.angular1x.context.router.IWebRouterContainer;
//import ghost.framework.web.angular1x.context.router.IWebRouterItem;
//import ghost.framework.web.angular1x.context.router.IWebRouterNavMenuTitle;
//import ghost.framework.web.context.resource.AbstractWebResourceResolver;
//import ghost.framework.web.context.io.IWebResource;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Map;
//
///**
// * package: ghost.framework.web.angular1x.container.plugin.http.response
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:路由资源解析器
// * @Date: 2020/3/15:15:53
// */
//@Component
//public final class WebRouterResourceResolver extends AbstractWebResourceResolver {
//    /**
//     * 注入模块路由容器接口
//     */
//    @Autowired
//    @Module("ghost.framework.web.module")
//    private IWebRouterContainer routerContainer;
//
//    @Override
//    public boolean isResolver(String path, IWebResource resource) {
//        //遍历菜单标题
//        for (Map.Entry<String, IWebRouterNavMenuTitle> entry : routerContainer.entrySet()) {
//            IWebRouterNavMenuTitle title = entry.getValue();
//            //判断是否使用
//            if (title.isUse()) {
//                //遍历路由对象
//                for (Map.Entry<String, IWebRouterItem> itemEntry : entry.getValue().entrySet()) {
//                    IWebRouterItem item = itemEntry.getValue();
//                    //判断是否使用
//                    if (item.isUse() && path.startsWith("/" + item.getUrl())) {
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public void resolver(String path, HttpServletRequest request, HttpServletResponse response, IWebResource resource) throws IOException {
//        super.resolver(path, request, response, resource);
//    }
//}