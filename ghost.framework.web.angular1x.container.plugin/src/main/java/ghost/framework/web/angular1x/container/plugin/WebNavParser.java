package ghost.framework.web.angular1x.container.plugin;

import ghost.framework.web.angular1x.context.router.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * package: ghost.framework.web.html.container.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web左边菜单解析器
 * 菜单格式规范实例
 * <ul class="nav">
 *   菜单分隔标题
 *   <li class="hidden-folded padder m-t m-b-sm text-muted text-xs">
 *     <span translate="aside.nav.HEADER">Navigation</span>
 *   </li>
 *   菜单项
 *   <li>
 *     菜单标题
 *     <a href class="auto">
 *       菜单右边箭头
 *       <span class="pull-right text-muted">
 *         <i class="fa fa-fw fa-angle-right text"></i>
 *         <i class="fa fa-fw fa-angle-down text-active"></i>
 *       </span>
 *       菜单图标
 *       <i class="glyphicon glyphicon-stats icon text-primary-dker"></i>
 *       <span class="font-bold" translate="aside.nav.DASHBOARD">Dashboard</span>
 *     </a>
 *     子菜单内容
 *     <ul class="nav nav-sub dk">
 *       <li class="nav-sub-header">
 *         <a href>
 *           translate为多语言处理
 *           <span translate="aside.nav.DASHBOARD">Dashboard</span>
 *         </a>
 *       </li>
 *       <li ui-sref-active="active">
 *         <a ui-sref="app.dashboard-v1">
 *           <span>仪表盘 v1</span>
 *         </a>
 *       </li>
 *       <li ui-sref-active="active">
 *         <a ui-sref="app.dashboard-v2">
 *           <b class="label bg-info pull-right">N</b>
 *           <span>仪表盘 v2</span>
 *         </a>
 *       </li>
 *     </ul>
 *   </li>
 *   //分割线
 *   <li class="line dk"></li>
 * </ul>
 * @Date: 2020/3/13:23:50
 */
public class WebNavParser {
    /**
     * 前部
     */
    private final String fore = "<ul class=\"nav\">";
    /**
     * 尾部
     */
    private final String tail = "</ul>";

    public String getNav(IWebMenuRouterContainer routerContainer) {
        StringBuilder builder = new StringBuilder();
        builder.append(fore);
        //遍历左边菜单
        for (Map.Entry<String, IWebRouterMenuGroup> nav : routerContainer.entrySet()) {
//            //判断是否为左边菜单
//            if (nav.getValue() instanceof IWebRouterHeaderMenuGroup) {
//                //不为左边菜单继续
//                continue;
//            }
            if (nav.getValue() instanceof IWebRouterNavMenuGroup) {
                //
                if (nav.getValue().isUse()) {
                    //菜单分隔标题
                    builder.append("<li class=\"line dk\"></li>");
                    builder.append("<li class=\"hidden-folded padder m-t m-b-sm text-muted text-xs\">");
//                builder.append("<span translate=\"aside.nav.HEADER\">Navigation</span>");
                    //组名称
                    builder.append("<span translate=\"aside.nav." + nav.getValue().getGroup() + ".name\"></span>");
                    builder.append("</li>");
                    //遍历菜单标题
                    for (Map.Entry<String, IWebRouterNavMenuTitle> entry : ((IWebRouterNavMenuGroup) nav.getValue()).entrySet()) {
                        //获取左边菜单标题
                        IWebRouterNavMenuTitle title = entry.getValue();
                        //判断是否使用
                        if (title.isUse()) {
                            //菜单项
                            builder.append("<li>");
                            //菜单标题
                            builder.append("<a href class=\"auto\">");
                            builder.append("<span class=\"pull-right text-muted\">");
                            builder.append("<i class=\"fa fa-fw fa-angle-right text\"></i>");
                            builder.append("<i class=\"fa fa-fw fa-angle-down text-active\"></i>");
                            builder.append("</span>");
                            //图标
                            if (StringUtils.isEmpty(title.getIcon())) {
                                builder.append("<i style=\"width: 40px;height: 40px;\"></i>");
                            } else {
                                builder.append("<i class=\"" + title.getIcon() + "\"></i>");
                            }

                            builder.append("<span class=\"font-bold\" translate=\"aside.nav." + nav.getValue().getGroup() + "." + title.getTitle() + ".name\"></span>");
                            builder.append("</a>");

                            //菜单容器
                            builder.append("<ul class=\"nav nav-sub dk\">");
                            builder.append("<li class=\"nav-sub-header\">");
                            builder.append("<a href=\"\">");
                            builder.append("<span translate=\"aside.nav." + nav.getValue().getGroup() + "." + title.getTitle() + ".name\"></span>");
                            builder.append("</a>");
                            builder.append("</li>");
                            //遍历路由对象
                            for (Map.Entry<String, IWebRouterItem> itemEntry : entry.getValue().entrySet()) {
                                IWebRouterItem item = itemEntry.getValue();
                                //菜单图标
//                    if (!StringUtils.isEmpty(item.getIcon())) {
//                        builder.append("<i class=\"" + item.getIcon() + "\"></i>");
//                    }
                                builder.append("<li ui-sref-active=\"active\" class=\"active\">");
                                if (StringUtils.isEmpty(item.getTitle())) {
                                    builder.append("<a ui-sref=" + item.getScope() + "." + item.getUrl() + ">");
                                } else {
//                                    builder.append("<a ui-sref=" + item.getScope() + "." + item.getUrl() + " title=\"" + item.getTitle() + "\">");
                                    builder.append("<a ui-sref=" + item.getScope() + "." + item.getUrl() + " title=\"{{ 'aside.nav." + nav.getValue().getGroup() + "." + title.getTitle() + "." + item.getName() + ".title' | translate }}\">");
                                }
                                builder.append("<span translate=\"aside.nav." + nav.getValue().getGroup() + "." + title.getTitle() + "." + item.getName() + ".name\"></span>");
//                                builder.append("<span>{{'" + item.getTitle() + "'|translate:'{aside.nav." + nav.getValue().getGroup() + "." + title.getTitle() + "." + item.getName() + ".name:string}'}}</span>");
                                builder.append("</a>");
                                builder.append("</li>");
                            }
                            builder.append("</ul>");

                            builder.append("</li>");
                        }
                    }
                }
            }
        }
        builder.append(tail);
        return builder.toString();
    }
}