package ghost.framework.web.setting.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * package: ghost.framework.web.setting.entity
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:网址配置表
 * @Date: 2020/6/8:14:16
 */
@Entity
@Table(name= "web_setting_c3e671d6",
        indexes = {
                @Index(name = "uk", columnList = "locale", unique = true),
                @Index(name = "pk", columnList = "title,edit_time,url,copyright,locale")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WebSettingEntity implements Serializable {
    private static final long serialVersionUID = -795347075878434604L;
    /**
     * 语言区域
     * 比如格式zh-CN
     */
    @Id
    @Column(name = "locale", nullable = false, updatable = false, length = 20, columnDefinition = "char(20)")
    @NotNull(message = "null error")
    @Length(max = 20, min = 1, message = "length error")
    private String locale;

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getLocale() {
        return locale;
    }
    /**
     * 消息标签
     */
    @NotNull(message = "null error")
    @Length(max = 128, min = 1, message = "length error")
    @Column(name = "title", nullable = false, updatable = false, length = 128)
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Length(max = 255, message = "length error")
    @Column(name = "copyright", length = 255)
    private String copyright;

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getCopyright() {
        return copyright;
    }

    @Length(max = 255, message = "length error")
    @Column(name = "url", length = 255)
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @NotNull(message = "null error")
    @Column(name = "edit_time", nullable = false)
    private Date editTime;

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }
}