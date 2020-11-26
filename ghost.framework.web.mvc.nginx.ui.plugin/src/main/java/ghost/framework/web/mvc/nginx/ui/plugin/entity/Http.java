package ghost.framework.web.mvc.nginx.ui.plugin.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name= "nginx_ui_http_5717b370",
		indexes = {
				@Index(name = "uk", columnList = "id", unique = true),
//				@Index(name = "pk", columnList = "id,name,create_time,status,description")
		})
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Http extends TableBase {
	private static final long serialVersionUID = 8719979160478490156L;
	private String name;
	private String value;
	private String unit;

	Long seq;

	public Http(){}

	public Http(String name, String value, Long seq) {
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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
