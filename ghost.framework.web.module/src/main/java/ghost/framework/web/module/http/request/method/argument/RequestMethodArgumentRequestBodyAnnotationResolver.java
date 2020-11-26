package ghost.framework.web.module.http.request.method.argument;

import com.fasterxml.jackson.databind.ObjectMapper;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.order.FirstOrder;
import ghost.framework.context.parameter.NameParameter;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.web.context.bens.annotation.HandlerMethodArgumentResolver;
import ghost.framework.web.context.bind.annotation.RequestBody;
import ghost.framework.web.context.bind.annotation.RequestMethod;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.http.request.method.argument.AbstractRequestMethodArgumentAnnotationResolver;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.web.context.http.request.method
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:解析 {@link RequestBody} 注释参数
 * @Date: 2020/2/29:0:19
 */
@FirstOrder
@HandlerMethodArgumentResolver
public class RequestMethodArgumentRequestBodyAnnotationResolver
        extends AbstractRequestMethodArgumentAnnotationResolver {
    /**
     *
     */
    private final Class<? extends Annotation> annotation = RequestBody.class;

    /**
     * 获取注释
     *
     * @return
     */
    @Override
    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }
    /**
     * 重写只判断注释
     * @param request
     * @param response
     * @param requestMethod 请求函数
     * @param parameter     判断是否可以解析的参数
     * @return
     * @throws ResolverException
     */
    @Override
    public boolean isResolver(HttpServletRequest request, HttpServletResponse response, IHttpRequestMethod requestMethod, NameParameter parameter) throws ResolverException {
        return super.isAnnotation(request, response, requestMethod, parameter);
    }
    /**
     * 注入应用json序列化
     */
    @Autowired
    @Application
    private ObjectMapper mapper;

    /**
     * @param requestMethod 请求函数
     * @param parameter     控制器函数参数
     * @return
     * @throws ResolverException
     */
    @Override
    public Object resolveArgument(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                  @NotNull IHttpRequestMethod requestMethod, @NotNull NameParameter parameter) throws ResolverException {
        //判断解析模式
        if (!request.getMethod().equals(RequestMethod.POST.name())) {
            throw new ResolverException(parameter.getParameter().toString());
        }
//            try {
//                //设置工厂
//                DiskFileItemFactory factory = new DiskFileItemFactory();
//                //设置缓冲区大小5M
//                //factory.setSizeThreshold(1024*1024*5);
//                //设置临时文件
//                //factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
//                //判断解析类型
//                ServletFileUpload upload = new ServletFileUpload(factory);
//                //判断编码
//                if (context.getRequest().getCharacterEncoding() == null) {
//                    upload.setHeaderEncoding("UTF-8");
//                } else {
//                    upload.setHeaderEncoding(context.getRequest().getCharacterEncoding());
//                }
//                //解析结果放list
//                List<FileItem> list = upload.parseRequest(context.getRequest());
//                System.out.println("表单数据项数：" + list.size());
//                for (FileItem item : list) {
//                    System.out.println("表单数据项数：" + list.size());
//                }
//                return null;
//            } catch (Exception e) {
//                throw new ResolverException(e.getMessage(), e);
//            }
//        }
        //读取表单流字符内容
        try (InputStreamReader inputStreamReader = new InputStreamReader(request.getInputStream(), request.getCharacterEncoding())) {
            try (BufferedReader streamReader = new BufferedReader(inputStreamReader)) {
                StringBuilder responseStrBuilder = new StringBuilder();
                String inputStr;
                while ((inputStr = streamReader.readLine()) != null) {
                    responseStrBuilder.append(inputStr);
                }
                //将字符内容转换对象
                String param = responseStrBuilder.toString();
                if (logger.isDebugEnabled()) {
                    logger.debug(param);
                }
                return this.mapper.readValue(param, parameter.getType());
            }
        } catch (Exception e) {
            throw new ResolverException(e.getMessage(), e);
        }
    }
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(RequestMethodArgumentRequestBodyAnnotationResolver.class);
}