package ghost.framework.web.angular1x.container.plugin;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.module.Module;
import ghost.framework.context.converter.json.MapToJsonConverter;
import ghost.framework.context.io.ResourceBytes;
import ghost.framework.context.utils.AssemblyUtil;
import ghost.framework.util.MimeTypeUtils;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.angular1x.context.menu.IWebHeaderMenuItem;
import ghost.framework.web.angular1x.context.menu.IWebHeaderMenuContainer;
import ghost.framework.web.angular1x.context.menu.annotation.WebHeaderMenu;
import ghost.framework.web.angular1x.context.router.IWebMenuRouterContainer;
import ghost.framework.web.angular1x.context.router.IWebRouterContainer;
import ghost.framework.web.context.bind.annotation.PathVariable;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.bind.annotation.RestController;
import ghost.framework.web.context.locale.IWebI18nLayoutContainer;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;
/**
 * package: ghost.framework.web.angular1x.container.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:作为html的容器控制器
 * @Date: 2020/3/5:12:55
 */
@RestController()
public class HtmlContainerRestController extends ControllerBase {
    /**
     * 注入模块路由容器接口
     * 带菜单的路由容器
     */
    @Autowired
    @Module//("ghost.framework.web.module")
    private IWebMenuRouterContainer menuRouterContainer;
    /**
     * 注入模块路由容器接口
     * 不不带菜单的路由容器
     */
    @Autowired
    @Module//("ghost.framework.web.module")
    private IWebRouterContainer routerContainer;
    /**
     * 注入顶部菜单容器接口
     */
    @Autowired
    private IWebHeaderMenuContainer headerMenuContainer;
    @Autowired
    @Application
    private MapToJsonConverter jsonConverter;
    /**
     * nav菜单解析器
     */
    private final WebNavParser navParser = new WebNavParser();
    /**
     * 路由解析器
     */
    private final WebRouterParser routerParser = new WebRouterParser();
    /**
     * 折叠按钮
     */
    private final String navbarCollapseButtons = "<!-- buttons -->\n" +
            "\t<div class=\"nav navbar-nav hidden-xs\">\n" +
            "\t\t<a href class=\"btn no-shadow navbar-btn\" ng-click=\"app.settings.asideFolded = !app.settings.asideFolded\">\n" +
            "\t\t\t<i class=\"fa {{app.settings.asideFolded ? 'fa-indent' : 'fa-dedent'}} fa-fw\"></i>\n" +
            "\t\t</a>\n" +
            "\t</div>\n" +
            "\t<!-- / buttons -->";
    /**
     * 前缀
     */
    private final String navbarCollapsePrefix = "<!-- navbar collapse -->\n" +
            "<div class=\"collapse pos-rlt navbar-collapse box-shadow {{app.settings.navbarCollapseColor}}\">";
    /**
     * 后缀
     */
    private final String navbarCollapseSuffix = "</div>\n" +
            "<!-- / navbar collapse -->";
    /**
     * 前缀
     * 右边位置
     */
    private final String nabarRightPrefix = "<!-- nabar right -->\n" +
            "\t<ul class=\"nav navbar-nav navbar-right\">";
    /**
     * 后缀
     * 右边位置
     */
    private final String nabarRightSuffix = "</ul>\n" +
            "\t<!-- / navbar right -->";
    /**
     * 头导航html内容
     */
    private final String navbarHeaderPrefix = "<!-- navbar header -->\n" +
            "<div class=\"navbar-header {{app.settings.navbarHeaderColor}}\">\n" +
            "\t<button class=\"pull-right visible-xs dk\" ui-toggle-class=\"show\" data-target=\".navbar-collapse\">\n" +
            "          <i class=\"glyphicon glyphicon-cog\"></i>\n" +
            "        </button>\n" +
            "\t<button class=\"pull-right visible-xs\" ui-toggle-class=\"off-screen\" data-target=\".app-aside\" ui-scroll-to=\"app\">\n" +
            "          <i class=\"glyphicon glyphicon-align-justify\"></i>\n" +
            "        </button>\n" +
            "\t<!-- brand -->";
    private final String navbarHeaderSuffix = "<!-- / brand -->\n" +
            "</div>\n" +
            "<!-- / navbar header -->";
    /**
     *
     * @return
     */
    @RequestMapping(value = "/tpl/blocks/header.html", produces = MimeTypeUtils.TEXT_HTML_VALUE)
    public String getHeader() {
        //连接模板格式
        //"<p style=\"margin:0;padding:0;\" ng-include=\"'a440a0d8/headerSearchMenu.html'\"></p>"+
        //左边logo
        String r = navbarHeaderPrefix;
        //左边模板
        for (IWebHeaderMenuItem headerMenu : headerMenuContainer.getList()) {
            if (headerMenu.getPosition() == WebHeaderMenu.Position.left) {
                r += splicingNabarHtml(headerMenu);
            }
        }
        r += navbarHeaderSuffix;
        //开始
        r += navbarCollapsePrefix;
        //折叠按钮
        r += navbarCollapseButtons;
        //中间模板
        for (IWebHeaderMenuItem headerMenu : headerMenuContainer.getList()) {
            if (headerMenu.getPosition() == WebHeaderMenu.Position.center) {
                r += splicingNabarHtml(headerMenu);
            }
        }
        r += "\n";
        //右边菜单容器
        r += nabarRightPrefix;
        //右边模板
        for (IWebHeaderMenuItem headerMenu : headerMenuContainer.getList()) {
            if (headerMenu.getPosition() == WebHeaderMenu.Position.right) {
                r += splicingNabarHtml(headerMenu);
            }
        }
        r += "\n";
        r += nabarRightSuffix;
        //结束
        r += navbarCollapseSuffix;
        return r;
    }

    /**
     *
     * @param headerMenu
     * @return
     */
    private String splicingNabarHtml(IWebHeaderMenuItem headerMenu) {
        String e = "\n";
        if (headerMenu.getElementName().isEmpty()) {
            e += "<p";
        } else {
            e += "<" + headerMenu.getElementName();
        }
        //连接
        e += " ng-include=\"'" + headerMenu.getTemplateUrl() + "'\"";
        //扩展属性
        for (String a : headerMenu.getElementAttributes()) {
            e += " " + a;
        }
        //
        if (!headerMenu.getElementClass().isEmpty()) {
            e += " class=\"" + headerMenu.getElementClass() + "\"";
        }
        if (!headerMenu.getElementStyle().isEmpty()) {
            e += " style=\"" + headerMenu.getElementStyle() + "\"";
        }
        //
        if (headerMenu.getElementName().isEmpty()) {
            e += "></p>";
        } else {
            e += "></" + headerMenu.getElementName() + ">";
        }
        return e;
    }
    /**
     * 获取路由
     *
     * @return
     */
    @RequestMapping(value = "/router.js", produces = MimeTypeUtils.APPLICATION_X_JAVASCRIPT_VALUE)
    public String getRouter() {
        return routerParser.getRouter(this.menuRouterContainer, routerContainer);
    }

    /**
     * 获取菜单
     *
     * @return
     */
    @RequestMapping(value = "/nav.html", produces = MimeTypeUtils.TEXT_HTML_VALUE)
    public String getNav() {
        return navParser.getNav(this.menuRouterContainer);
    }

    /**
     * 注入国际化容器接口
     */
    @Autowired
    @Module
    private IWebI18nLayoutContainer layoutContainer;

    /**
     * 获取语言json文件
     * 作为web容器国际化获取的语言数据
     * 每个模块或每个插件都单独区域语言在此合并返回给
     */
    @RequestMapping(value = "i18n/{locale}.json")
    public void get(@PathVariable String locale, HttpServletResponse response) {
        InputStream stream = null;
        try {
            //获取国际化容器
            Map<Object, Object> map = layoutContainer.getLocale(locale);
            if (map == null || map.size() == 0) {
                ResourceBytes resourceBytes = AssemblyUtil.getResourceBytes(HtmlContainerRestController.class, "i18n/" + locale + ".json");
                stream = new ByteArrayInputStream(resourceBytes.getBytes());
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, response.getCharacterEncoding()));
                response.getOutputStream().write(IOUtils.toByteArray(streamReader));
            } else {
                //获取json转换接口
//                MapToJsonConverter jsonConverter = (MapToJsonConverter) jsonConverterContainer.getConverter(MapToJsonConverter.class);
                response.getOutputStream().write(jsonConverter.toString(map).getBytes(response.getCharacterEncoding()));
            }
            response.setHeader("Content-Type", "application/json");
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            this.exception(e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {

                }
            }
        }
    }
}