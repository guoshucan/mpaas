package ghost.framework.web.mvc.nginx.ui.plugin.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name= "nginx_ui_upstream_server_62c0875",
		indexes = {
				@Index(name = "uk", columnList = "id", unique = true),
//				@Index(name = "pk", columnList = "id,name,create_time,status,description")
		})
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UpstreamServer extends TableBase {
	private static final long serialVersionUID = 7219317445671634679L;
	String upstreamId;
	String server;
	Integer port;
	Integer weight;
	
	Integer failTimeout; // 失败超时
	Integer maxFails; // 失败次数
	
	String status; // 状态策略
	

	Integer monitorStatus=0;// 监控状态 0 不通 1
	
	
	public Integer getMonitorStatus() {
		return monitorStatus;
	}
	public void setMonitorStatus(Integer monitorStatus) {
		this.monitorStatus = monitorStatus;
	}
	public Integer getFailTimeout() {
		return failTimeout;
	}
	public void setFailTimeout(Integer failTimeout) {
		this.failTimeout = failTimeout;
	}
	public Integer getMaxFails() {
		return maxFails;
	}
	public void setMaxFails(Integer maxFails) {
		this.maxFails = maxFails;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getUpstreamId() {
		return upstreamId;
	}
	public void setUpstreamId(String upstreamId) {
		this.upstreamId = upstreamId;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	
	
}
