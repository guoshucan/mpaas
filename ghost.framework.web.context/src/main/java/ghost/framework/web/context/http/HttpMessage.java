package ghost.framework.web.context.http;

/**
 * package: ghost.framework.web.module.http
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-15:1:19
 */
/**
 * Represents the base interface for HTTP request and response messages.
 * Consists of {@link HttpHeaders}, retrievable via {@link #getHeaders()}.
 *
 * @author Arjen Poutsma
 * @since 3.0
 */
public interface HttpMessage {

    /**
     * Return the headers of this message.
     * @return a corresponding HttpHeaders object (never {@code null})
     */
    HttpHeaders getHeaders();

}
