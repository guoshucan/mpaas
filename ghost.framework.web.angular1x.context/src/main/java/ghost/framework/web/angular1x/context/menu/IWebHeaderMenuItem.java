package ghost.framework.web.angular1x.context.menu;

import ghost.framework.context.IGetDomain;
import ghost.framework.web.angular1x.context.menu.annotation.WebHeaderMenu;
import ghost.framework.web.angular1x.context.menu.annotation.WebHeaderMenus;

/**
 * package: ghost.framework.web.angular1x.context.router
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:头部菜单接口
 * {@link WebHeaderMenus}
 * {@link WebHeaderMenu}
 * @Date: 2020/8/17:23:32
 */
public interface IWebHeaderMenuItem extends IGetDomain {
    /**
     * 获取元素扩展属性
     * 比如ng-controller="e7a1375f" ng-init="init()"控制器格式{"ng-controller='e7a1375f'", "ng-init='init()'"}
     * @return
     */
    String[] getElementAttributes();
    /**
     * 获取头部菜单位置
     * @return
     */
    WebHeaderMenu.Position getPosition();

    /**
     * 获取菜单模板
     * @return
     */
    String getTemplateUrl();

    /**
     * 获取ng-include呈现的元素名称
     * 默认为p，等于呈现为<p></p>元素
     * @return 返回呈现的元素名称
     */
    String getElementName();

    /**
     * 获取ng-include呈现的元素样式
     * @return 返回呈现的元素样式
     */
    String getElementClass();

    /**
     * 获取ng-include呈现的元素样式
     * @return 返回呈现的元素样式
     */
    String getElementStyle();
}
