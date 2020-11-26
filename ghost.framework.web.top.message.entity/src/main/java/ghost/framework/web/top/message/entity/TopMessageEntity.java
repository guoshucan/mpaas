package ghost.framework.web.top.message.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * package: ghost.framework.web.top.message.entity
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:顶部消息表
 * @Date: 2020/6/7:22:56
 */
@Entity
@Table(name= "4463929951874fa7a9571da251265f61",
        indexes = {
                @Index(name = "uk", columnList = "id", unique = true),
                @Index(name = "pk", columnList = "id,title,create_time,status,icon,url")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TopMessageEntity implements Serializable {
        private static final long serialVersionUID = 67093856917003282L;
        /**
         * 主键id
         */
        @Length(max = 36, min = 36, message = "length error")
        @NotNull(message = "null error")
        @Id
        @Column(name = "id", updatable = false, nullable = false, unique = true, length = 36, columnDefinition = "char(36)")
        private String id;

        public String getId() {
                return id;
        }

        public void setId(String id) {
                this.id = id;
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

        /**
         * 创建时间
         */
        @NotNull(message = "null error")
        @Column(name = "create_time", nullable = false)
        private Date createTime;

        public Date getCreateTime() {
                return createTime;
        }

        public void setCreateTime(Date createTime) {
                this.createTime = createTime;
        }

        /**
         * 状态
         * 否为未阅读
         * 是为已经阅读
         */
        @Column(name = "status", nullable = false)
        private boolean status;

        public boolean getStatus() {
                return status;
        }

        public void setStatus(boolean status) {
                this.status = status;
        }

        /**
         * 地址
         */
        @Column(name = "url", length = 255, updatable = false, nullable = false, columnDefinition = "char(255)")
        @Length(max = 255, min = 7, message = "length error")
        @NotNull
        private String url;

        public void setUrl(String url) {
                this.url = url;
        }

        public String getUrl() {
                return url;
        }

        /**
         * 消息图标
         */
        @Column(name = "icon", length = 255, nullable = false, columnDefinition = "char(255)")
        @Length(max = 255, message = "length error")
        private String icon;

        public void setIcon(String icon) {
                this.icon = icon;
        }

        public String getIcon() {
                return icon;
        }

        /**
         * 消息内容
         */
        @Column(name = "content", nullable = false, updatable = false)
        @Lob
        private String content;

        public String getContent() {
                return content;
        }

        public void setContent(String content) {
                this.content = content;
        }
}