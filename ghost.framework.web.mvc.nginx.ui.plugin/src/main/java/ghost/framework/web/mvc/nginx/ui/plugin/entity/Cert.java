package ghost.framework.web.mvc.nginx.ui.plugin.entity;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name= "nginx_ui_cert_3aca388e",
		indexes = {
				@Index(name = "uk", columnList = "id", unique = true),
//				@Index(name = "pk", columnList = "id,name,create_time,status,description")
		})
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cert extends TableBase {
	private static final long serialVersionUID = -8494505054693497426L;
	@NotNull(message = "null error")
	@Length(max = 255, min = 1, message = "length error")
	@Column(name = "domain", nullable = false, unique = true, length = 255)
	String domain;// 域名

	String pem;
	String key;

//	@InitValue("0")
	Integer type = 0; // 获取方式 0 申请证书 1 手动上传

	Long makeTime; // 生成时间

//	@InitValue("0")
	Integer autoRenew = 0; // 自动续签

	String pemStr;
	String keyStr;

	String dnsType;
	String dpId;
	String dpKey;
	String aliKey;
	String aliSecret;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDnsType() {
		return dnsType;
	}

	public void setDnsType(String dnsType) {
		this.dnsType = dnsType;
	}

	public String getDpId() {
		return dpId;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId;
	}

	public String getDpKey() {
		return dpKey;
	}

	public void setDpKey(String dpKey) {
		this.dpKey = dpKey;
	}

	public String getAliKey() {
		return aliKey;
	}

	public void setAliKey(String aliKey) {
		this.aliKey = aliKey;
	}

	public String getAliSecret() {
		return aliSecret;
	}

	public void setAliSecret(String aliSecret) {
		this.aliSecret = aliSecret;
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

	public Integer getAutoRenew() {
		return autoRenew;
	}

	public void setAutoRenew(Integer autoRenew) {
		this.autoRenew = autoRenew;
	}

	public Long getMakeTime() {
		return makeTime;
	}

	public void setMakeTime(Long makeTime) {
		this.makeTime = makeTime;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
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

}
