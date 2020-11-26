package ghost.framework.web.module.http.request;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:处理请求接口
 * @Date: 3:48 2019/11/8
 */
public interface IHttpHandler
//        extends IHttpInterceptor
{
    /**
     * 请求处理
     *
     * @param target   request的目标，可以是一个url或者一个适配器。
     * @param request  不可变的request对象，可以被封装。
     * @param response response对象，可以被封装
     * @throws IOException
     * @throws ServletException
     */
     void handle(String target, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
}
