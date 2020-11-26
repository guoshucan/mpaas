package ghost.framework.web.angular1x.container.plugin;
import ghost.framework.web.angular1x.context.router.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * package: ghost.framework.web.html.container.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web路由解析器
 * @Date: 2020/3/13:23:50
 */
public class WebRouterParser {
    /**
     * 获取路由信息
     * @param menuRouterContainer 菜单路由容器
     * @param routerContainer 路由容器
     * @return
     */
    public String getRouter(IWebMenuRouterContainer menuRouterContainer, IWebRouterContainer routerContainer) {
        StringBuilder builder = new StringBuilder();
        builder.append("'use strict';");
        builder.append("\r\n");
/**
 * Config for the router
 */
        builder.append("angular.module('app')");
        builder.append(".run(['$rootScope','$state','$stateParams',function ($rootScope, $state,$stateParams){$rootScope.$state=$state;$rootScope.$stateParams=$stateParams;}])");
        builder.append(".config(['$stateProvider','$urlRouterProvider','JQ_CONFIG','MODULE_CONFIG',function($stateProvider,$urlRouterProvider,JQ_CONFIG,MODULE_CONFIG){");
        builder.append("var layout='tpl/app.html';");
        //遍历左边菜单
        for (Map.Entry<String, IWebRouterMenuGroup> nav : menuRouterContainer.entrySet()) {
            //判断是否为顶部菜单路由
//            if(nav.getValue() instanceof IWebRouterHeaderMenuGroup) {
//                continue;
//            }
            //判断是否为左边菜单路由
            if(nav.getValue() instanceof IWebRouterNavMenuGroup) {
                //遍历菜单标题
                for (Map.Entry<String, IWebRouterNavMenuTitle> entry : ((IWebRouterNavMenuGroup) nav.getValue()).entrySet()) {
                    //判断是否使用
                    if (entry.getValue().isUse()) {
                        //遍历路由对象
                        for (Map.Entry<String, IWebRouterItem> itemEntry : entry.getValue().entrySet()) {
                            IWebRouterItem item = itemEntry.getValue();
                            if (item.isUse())
                                //初始化路由地址
                                builder.append("$urlRouterProvider.otherwise('/" + item.getScope() + "/" + item.getUrl() + "');");
                            break;
                        }
                        break;
                    }
                }
            }
        }
        builder.append("$stateProvider.state('app',{abstract: true, url: '/app', templateUrl: layout})");
        //解析没有菜单的路由信息
        for (Map.Entry<String, IWebRouterItem> entry : routerContainer.entrySet()) {
            IWebRouterItem item = entry.getValue();
            //判断是否使用
            if (item.isUse()) {
                String controllers = null;
                for (String url : item.getController()) {
                    if (controllers == null) {
                        controllers = url;
                    } else {
                        controllers += "','" + url;
                    }
                }
                if (StringUtils.isEmpty(item.getTemplate())) {
                    builder.append(".state('" + item.getScope() + "." + item.getUrl() + "',{url:'/" + item.getUrl() + "',templateUrl:'" + item.getTemplateUrl() + "',resolve:load(['" + controllers + "'])})");
                } else {
                    builder.append(".state('" + item.getScope() + "',{url:'/" + item.getUrl() + "',template:'" + item.getTemplate() + "'})");
                }
            }
        }
        //遍历左边菜单
        for (Map.Entry<String, IWebRouterMenuGroup> nav : menuRouterContainer.entrySet()) {
            //判断是否为顶部菜单路由
//            if (nav.getValue() instanceof IWebRouterHeaderMenuGroup) {
//                continue;
//            }
            //判断是否为左边菜单路由
            if (nav.getValue() instanceof IWebRouterNavMenuGroup) {
                //遍历菜单标题
                for (Map.Entry<String, IWebRouterNavMenuTitle> entry : ((IWebRouterNavMenuGroup) nav.getValue()).entrySet()) {
                    //判断是否使用
                    if (entry.getValue().isUse()) {
                        //遍历路由对象
                        for (Map.Entry<String, IWebRouterItem> itemEntry : entry.getValue().entrySet()) {
                            IWebRouterItem item = itemEntry.getValue();
                            //判断是否使用
                            if (item.isUse()) {
                                String controllers = null;
                                for (String url : item.getController()) {
                                    if (controllers == null) {
                                        controllers = url;
                                    } else {
                                        controllers += "','" + url;
                                    }
                                }
                                if (StringUtils.isEmpty(item.getTemplate())) {
                                    builder.append(".state('" + item.getScope() + "." + item.getUrl() + "',{url:'/" + item.getUrl() + "',templateUrl:'" + item.getTemplateUrl() + "',resolve:load(['" + controllers + "'])})");
                                } else {
                                    builder.append(".state('" + item.getScope() + "',{url:'/" + item.getUrl() + "',template:'" + item.getTemplate() + "'})");
                                }
                            }
                        }
                    }
                }
            }
        }
        builder.append(";");
//        builder.append(".state('app.dashboard-v1',{url:'/dashboard-v1',templateUrl:'tpl/app_dashboard_v1.html',resolve:load(['js/controllers/chart.js'])})");
//        builder.append(".state('app.dashboard-v2',{url:'/dashboard-v2',templateUrl:'tpl/app_dashboard_v2.html',resolve:load(['js/controllers/chart.js'])})");
//        builder.append(".state('app.dashboard-v3',{url:'/dashboard-v3',templateUrl:'tpl/app_dashboard_v3.html',resolve:load(['js/controllers/chart.js'])});");
        builder.append("function load(srcs, callback) {");
        builder.append("return {");
        builder.append("deps: ['$ocLazyLoad', '$q',");
        builder.append("function( $ocLazyLoad, $q ){");
        builder.append("var deferred = $q.defer();");
        builder.append("var promise  = false;");
        builder.append("srcs=angular.isArray(srcs)?srcs:srcs.split(/\\s+/);");
        builder.append("if(!promise){");
        builder.append("promise = deferred.promise;");
        builder.append("}");
        builder.append("angular.forEach(srcs, function(src) {");
        builder.append("promise = promise.then( function(){");
        builder.append("if(JQ_CONFIG[src]){");
        builder.append("return $ocLazyLoad.load(JQ_CONFIG[src]);");
        builder.append("}");
        builder.append("angular.forEach(MODULE_CONFIG, function(module) {");
        builder.append("if( module.name == src){");
        builder.append("name = module.name;");
        builder.append("}else{");
        builder.append("name = src;");
        builder.append("}");
        builder.append("});");
        builder.append("return $ocLazyLoad.load(name);");
        builder.append("} );");
        builder.append("});");
        builder.append("deferred.resolve();");
        builder.append("return callback ? promise.then(function(){ return callback(); }) : promise;");
        builder.append("}]");
        builder.append("}");
        builder.append("}");
        builder.append("}");
        builder.append("]");
        builder.append(");");
        return builder.toString();
    }
}