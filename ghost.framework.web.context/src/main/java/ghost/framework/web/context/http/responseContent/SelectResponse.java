package ghost.framework.web.context.http.responseContent;

/**
 * 分页响应。
 */
public class SelectResponse extends DataResponse {
    private static final long serialVersionUID = 7426957949298149551L;

    private long count;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    private long pages;

    public long getPages() {
        return pages;
    }

    public void setPages(long pages) {
        this.pages = pages;
    }

    private int length;

    public void setLength(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }
}
