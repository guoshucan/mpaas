package ghost.framework.maven;
import ghost.framework.util.Assert;

import java.io.Serializable;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:maven仓库服务器。
 * @Date: 15:10 2018-07-14
 */
public final class MavenRepositoryServer implements Serializable {
    private static final long serialVersionUID = 8000615833907438276L;
    public MavenRepositoryServer() {
    }
    public MavenRepositoryServer(String id, String type, String url) {
        this(id, type, null, null, url);
    }
    public MavenRepositoryServer(String id, String type, String username, String password, String url) {
        Assert.notNullOrEmpty(id, "~MavenRepositoryServer is id null error");
        Assert.notNullOrEmpty(url, "~MavenRepositoryServer is url null error");
        this.id = id;
        this.type = type;
        this.username = username;
        this.password = password;
        this.url = url;
    }
    public MavenRepositoryServer(String id, String url) {
        this(id, null, null, null, url);
    }
    @Override
    public String toString() {
        return "{id:" + id + ",type:" + type + ",username:" + username + ",password:" + password + ",url:" + url + "}";
    }

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    /**
     * maven仓库类型。
     * 默认为：default。
     */
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * maven仓库地址。
     * 实例内容。
     * id:null,type:default:url:http://maven.aliyun.com/nexus/content/groups/public/
     * id:null,type:null:url:http://maven.net.cn/content/groups/public/
     * id:null,type:null:url:http://maven.jahia.org/maven2/
     * id:null,type:null:url:https://repo1.maven.org/maven2/
     * id:central,type:null:url:http://central.maven.org/maven2/
     */
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}