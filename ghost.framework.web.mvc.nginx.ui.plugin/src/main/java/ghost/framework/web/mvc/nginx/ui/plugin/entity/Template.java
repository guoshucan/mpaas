package ghost.framework.web.mvc.nginx.ui.plugin.entity;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name= "nginx_ui_template_3d132b72",
		indexes = {
				@Index(name = "uk", columnList = "id", unique = true),
//				@Index(name = "pk", columnList = "id,name,create_time,status,description")
		})
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Template extends TableBase {
	private static final long serialVersionUID = -4227125015641981392L;
	String name;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
