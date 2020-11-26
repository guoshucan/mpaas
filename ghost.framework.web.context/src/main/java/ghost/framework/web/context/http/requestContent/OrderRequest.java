package ghost.framework.web.context.http.requestContent;

/**
 * package: ghost.framework.web.context.http.requestContent
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/4/6:19:33
 */
public class OrderRequest {
    private boolean ascending;

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public boolean isAscending() {
        return ascending;
    }

    private boolean ignoreCase;

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    private String propertyName;
}
