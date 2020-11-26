package ghost.framework.web.setting.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * package: ghost.framework.web.setting.entity
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:网址配置表
 * @Date: 2020/6/8:14:16
 */
@Entity
@Table(name= "web_setting_logo_c213c764",
        indexes = {
                @Index(name = "uk", columnList = "id", unique = true),
                @Index(name = "pk", columnList = "id,logo_url")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WebSettingLogoEntity implements Serializable {
    private static final long serialVersionUID = -4082338321604745038L;
    /**
     * 主键id
     */
    @Id
    @Column(name = "id", updatable = false, nullable = false, length = 36, columnDefinition = "char(36)")
    @NotNull(message = "null error")
    @Length(max = 36, min = 36, message = "length error")
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Length(max = 255, message = "length error")
    @Column(name = "logo_url", length = 255)
    private String logoUrl;

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    @Basic(fetch = FetchType.LAZY)
    @Lob
    @Column(name = "logo")
    private byte[] logo;

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }
}