package ghost.framework.web.mvc.nginx.ui.plugin.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name= "nginx_ui_www_89253565",
		indexes = {
				@Index(name = "uk", columnList = "id", unique = true),
//				@Index(name = "pk", columnList = "id,name,create_time,status,description")
		})
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Www extends TableBase {

	private static final long serialVersionUID = -4315958923037485638L;
	String dir;
	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

}
