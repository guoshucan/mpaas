package ghost.framework.web.mvc.nginx.ui.plugin.entity;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name= "nginx_ui_basic_813e2de0",
		indexes = {
				@Index(name = "uk", columnList = "id", unique = true),
//				@Index(name = "pk", columnList = "id,name,create_time,status,description")
		})
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Basic extends TableBase {
	private static final long serialVersionUID = -9069507189930743666L;
	String name;
	String value;
	Long seq;


	public Basic() {

	}

	public Basic(String name, String value, Long seq) {
		this.name = name;
		this.value = value;
		this.seq = seq;
	}


	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
