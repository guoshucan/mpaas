package ghost.framework.web.context.http.requestContent;

import java.util.List;

/**
 * package: ghost.framework.web.context.http.requestContent
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:请求id列表
 * @Date: 2020/4/4:15:51
 */
public class RequestIds extends Request {
    private List<String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}