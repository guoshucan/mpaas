package ghost.framework.web.mvc.nginx.ui.plugin.ext;

import ghost.framework.web.mvc.nginx.ui.plugin.entity.Param;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Template;

import java.util.List;

public class TemplateExt {
	Template template;
	Integer count;
	List<Param> paramList;
	
	
	public List<Param> getParamList() {
		return paramList;
	}
	public void setParamList(List<Param> paramList) {
		this.paramList = paramList;
	}
	public Template getTemplate() {
		return template;
	}
	public void setTemplate(Template template) {
		this.template = template;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
	
}
