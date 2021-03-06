package ghost.framework.web.mvc.nginx.ui.plugin.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name= "nginx_ui_server_812c1cab",
		indexes = {
				@Index(name = "uk", columnList = "id", unique = true),
//				@Index(name = "pk", columnList = "id,name,create_time,status,description")
		})
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Server extends TableBase {
	private static final long serialVersionUID = -8842914487055268371L;
	String serverName;
	String listen;

	Integer def; 
	
//	@InitValue("0")
	Integer rewrite=0; // 0否 1是
//	@InitValue("0")
	Integer ssl=0; // 0 否 1是
//	@InitValue("0")
	Integer http2=0; // 0否 1是
	String pem;
	String key;
	// 代理类型
//	@InitValue("0")
	Integer proxyType=0; //  0 http 1 tcp
	String proxyUpstreamId;
	
	String pemStr;
	String keyStr;
	
//	@InitValue("true")
	Boolean enable=true; // 是否启用
	
	
	public Integer getDef() {
		return def;
	}
	public void setDef(Integer def) {
		this.def = def;
	}
	public Boolean getEnable() {
		return enable;
	}
	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
	public Integer getHttp2() {
		return http2;
	}
	public void setHttp2(Integer http2) {
		this.http2 = http2;
	}
	public String getPemStr() {
		return pemStr;
	}
	public void setPemStr(String pemStr) {
		this.pemStr = pemStr;
	}
	public String getKeyStr() {
		return keyStr;
	}
	public void setKeyStr(String keyStr) {
		this.keyStr = keyStr;
	}
	public String getProxyUpstreamId() {
		return proxyUpstreamId;
	}
	public void setProxyUpstreamId(String proxyUpstreamId) {
		this.proxyUpstreamId = proxyUpstreamId;
	}
	public Integer getProxyType() {
		return proxyType;
	}
	public void setProxyType(Integer proxyType) {
		this.proxyType = proxyType;
	}
	public Integer getSsl() {
		return ssl;
	}
	public void setSsl(Integer ssl) {
		this.ssl = ssl;
	}
	public String getPem() {
		return pem;
	}
	public void setPem(String pem) {
		this.pem = pem;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getListen() {
		return listen;
	}
	public void setListen(String listen) {
		this.listen = listen;
	}

	public Integer getRewrite() {
		return rewrite;
	}

	public void setRewrite(Integer rewrite) {
		this.rewrite = rewrite;
	}

	

}
