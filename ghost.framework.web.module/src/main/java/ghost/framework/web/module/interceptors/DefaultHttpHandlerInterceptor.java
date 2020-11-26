package ghost.framework.web.module.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.web.module.http.request.IHttpHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;

/**
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:默认http处理拦截器
 * @Date: 3:50 2019/11/8
 */
public class DefaultHttpHandlerInterceptor implements IHttpHandler {
    /**
     * 请求处理
     *
     * @param target   request的目标，可以是一个url或者一个适配器。
     * @param request  不可变的request对象，可以被封装。
     * @param response response对象，可以被封装
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println(target);
        //获取文件上传域
        for (Part part : request.getParts()) {
            System.out.println("上传文件的类型为:" + part.getContentType() + "<br/>");
            System.out.println("上传文件的大小为:" + part.getSize());
            //获取该文件的上传域
            Collection<String> headerNames = part.getHeaderNames();
            for (String headerName : headerNames) {
                System.out.println(headerName + "--->" + part.getHeader(headerName) + "<br/>");
            }
        }
    }

    /**
     * delegation = true 如果模块未定义绑定该类型侧往上层获取绑定对象
     * 模块或应用全局json处理对象
     */
    @Autowired
    private ObjectMapper mapper;
}