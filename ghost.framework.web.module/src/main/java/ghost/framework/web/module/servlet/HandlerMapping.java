package ghost.framework.web.module.servlet;
import ghost.framework.beans.annotation.constraints.Nullable;
import javax.servlet.http.HttpServletRequest;
/**
 * 请求处理地图接口
 */
public interface HandlerMapping {
	/**
	 * Name of the {@link ServerWebExchange#getAttributes() attribute} containing
	 * the set of producible MediaType's applicable to the mapped handler.
	 * <p>Note: This attribute is not required to be supported by all
	 * HandlerMapping implementations. Handlers should not necessarily expect
	 * this request attribute to be present in all scenarios.
	 */
	String PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE = HandlerMapping.class.getName() + ".producibleMediaTypes";
	/**
	 * 获取处理执行链
	 * 按照相同拦截路径的处理存储在依赖执行链
	 * 然后按照指定的排序进行遍历执行，排序值小优先执行
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Nullable
	HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception;
}
