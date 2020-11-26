package ghost.framework.web.module.servlet.context;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.module.IModule;
import ghost.framework.util.Assert;
import ghost.framework.web.context.http.IHttpControllerContainer;
import ghost.framework.web.context.servlet.context.IServletContextContainer;
import ghost.framework.web.context.servlet.context.ServletContextInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * {@link ServletContextInitializer} 容器
 */
@Component
public class ServletContextContainer extends AbstractCollection<ServletContextInitializer> implements IServletContextContainer {

	/**
	 * 注入模块接口
	 */
	@Autowired
	private IModule module;
	/**
	 * {@link ServletContextInitializer} 列表
	 */
	private List<ServletContextInitializer> sortedList = new ArrayList<>();

	/**
	 * 添加 {@link ServletContextInitializer}
	 *
	 * @param servletContextInitializer
	 * @return
	 */
	@Override
	public boolean add(ServletContextInitializer servletContextInitializer) {
		Assert.notNull(servletContextInitializer, "add is servletContextInitializer null error");
		return this.sortedList.add(servletContextInitializer);
	}

	@Override
	public Iterator<ServletContextInitializer> iterator() {
		return this.sortedList.iterator();
	}

	/**
	 * 删除 {@link ServletContextInitializer}
	 *
	 * @param o
	 * @return
	 */
	@Override
	public boolean remove(Object o) {
		return this.sortedList.remove(o);
	}

	/**
	 * 获取 {@link ServletContextInitializer} 数量
	 *
	 * @return //
	 */
	@Override
	public int size() {
		return this.sortedList.size();
	}

	/**
	 * @param servletContext the {@code ServletContext} to initialize
	 * @throws ServletException
	 */
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		this.servletContext = servletContext;
		synchronized (this.sortedList) {
			for (ServletContextInitializer initializer : this.sortedList) {
				initializer.onStartup(servletContext);
			}
		}
		//刷新http请求函数容器
		this.module.getBean(IHttpControllerContainer.class).refreshHttpRequestMethodContainer();
	}


	private ServletContext servletContext;
	/**
	 * 获取 ServletContext
	 * @return
	 */
	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}
}