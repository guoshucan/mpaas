package ghost.framework.web.context.http.responseContent;

import java.io.Serializable;

/**
 * 响应基础类。
 */
public class Response implements Serializable {
    private static final long serialVersionUID = 3470879827471643008L;

    /**
     * 初始化返回请求。
     */
    public Response() {
    }

    /**
     * 初始化返回请求。
     *
     * @param message 设置错误消息。
     */
    public Response(String message) {
        this.message = message;
    }

    /**
     * 初始化返回请求。
     *
     * @param code    设置错误代码。
     * @param message 设置错误消息。
     */
    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 初始化返回请求。
     *
     * @param code    设置错误代码。
     * @param message 设置错误消息。
     * @param content 设置返回内容。
     */
    public Response(int code, String message, String content) {
        this.code = code;
        this.message = message;
        this.content = content;
    }

    public static Response success() {
        return new Response(0, "success");
    }

    public static Response error(int code) {
        return new Response(code, "error");
    }

    public static Response error(int code, String message) {
        return new Response(code, message);
    }

    /**
     * 错误代码。
     */
    private int code = 0;

    /**
     * 获取错误代码。
     *
     * @return
     */
    public int getCode() {
        return this.code;
    }

    /**
     * 设置错误代码。
     *
     * @param code
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 错误消息。
     */
    private String message = "success";

    /**
     * 获取错误消息。
     *
     * @return
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * 设置错误消息。
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 返回请求内容。
     */
    private Object content;

    /**
     * 获取返回请求内容。
     *
     * @return
     */
    public Object getContent() {
        return this.content;
    }

    /**
     * 设置返回请求内容。
     *
     * @param content
     */
    public void setContent(Object content) {
        this.content = content;
    }

    /**
     * 设置为错误状态。
     */
    public void setError() {
        this.message = "error";
    }

    /**
     * 设置为错误状态码。
     *
     * @param code 设置错误状态码。
     */
    public void setError(int code) {
        this.code = code;
        this.message = "error";
    }

    /**
     * 设置为错误状态。
     *
     * @param code    设置错误状态码。
     * @param content 设置错误内容。
     */
    public void setError(int code, String content) {
        this.code = code;
        this.content = content;
        this.message = "error";
    }

    /**
     * 设置为完成。
     */
    public void setComplete() {
        this.code = 0;
        this.message = "success";
    }

    /**
     * 设置为完成。
     *
     * @param content 设置完成内容。
     */
    public void setComplete(String content) {
        this.content = content;
        this.code = 0;
        this.message = "ok";
    }

    /**
     * 设置为失败。
     */
    public void setFailure() {
        this.message = "failure";
    }

    /**
     * 设置为失败。
     *
     * @param code    失败代码。
     * @param content 失败内容。
     */
    public void setFailure(int code, String content) {
        this.code = code;
        this.content = content;
        this.message = "failure";
    }

    public boolean isSuccess() {
        return this.message.equals("success");
    }
}