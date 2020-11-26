package ghost.framework.web.mvc.nginx.ui.plugin.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name= "nginx_ui_upstream_e87e7e70",
		indexes = {
				@Index(name = "uk", columnList = "id", unique = true),
//				@Index(name = "pk", columnList = "id,name,create_time,status,description")
		})
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Upstream extends TableBase {
	private static final long serialVersionUID = 2497110573522599939L;
	String name;
	String tactics; // 负载策略
	
	// 代理类型
//	@InitValue("0")
	Integer proxyType=0; //  0 http 1 tcp
	
//	@InitValue("0")
	Integer monitor=0;
	
	
	public Integer getMonitor() {
		return monitor;
	}
	public void setMonitor(Integer monitor) {
		this.monitor = monitor;
	}
	public Integer getProxyType() {
		return proxyType;
	}
	public void setProxyType(Integer proxyType) {
		this.proxyType = proxyType;
	}
	
	public String getTactics() {
		return tactics;
	}

	public void setTactics(String tactics) {
		this.tactics = tactics;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
