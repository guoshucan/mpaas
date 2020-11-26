package ghost.framework.web.context.http.requestContent;

import java.util.Date;
import java.util.List;

/**
 * package: ghost.framework.web.context.http.responseContent
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/3/17:1:30
 */
public class SelectRequest extends Request{
    private List<OrderRequest> order;

    public List<OrderRequest> getOrder() {
        return order;
    }

    public void setOrder(List<OrderRequest> order) {
        this.order = order;
    }

    private int start ;

    public void setStart(int start) {
        this.start = start;
    }

    public int getStart() {
        return start;
    }

    private int length;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
    private String key;

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
    public Date startTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public Date endTime;

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
