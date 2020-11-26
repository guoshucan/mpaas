package ghost.framework.web.context.http.responseContent;

/**
 * 数据对象响应。
 */
public class DataResponse extends Response {
    private static final long serialVersionUID = 5384704683110719242L;
    public DataResponse(){
        super();
    }
    /**
     * 数据对象。
     */
    private Object data;

    public DataResponse(int code, String message) {
        super (code, message);
    }
    public DataResponse(int code, String message, Object data) {
        super (code, message);
        this.data = data;
    }

    public static DataResponse success() {
        return new DataResponse(0, "success");
    }
    public static DataResponse success(Object data) {
        return new DataResponse(0, "success", data);
    }
    public static DataResponse error(int code) {
        return new DataResponse(code, "error");
    }
    public static DataResponse error(int code, String message) {
        return new DataResponse(code, message);
    }
    /**
     * 获取数据对象。
     *
     * @return
     */
    public Object getData() {
        return data;
    }

    /**
     * 设置数据对象。
     *
     * @param data
     */
    public void setData(Object data) {
        this.data = data;
    }
}
