package ghost.framework.web.module.http.request.method.returnValue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.resolver.ReturnValueResolverException;
import ghost.framework.web.context.bens.annotation.HandlerMethodReturnValueResolver;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.http.MediaType;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.http.request.method.returnValue.AbstractRequestMethodReturnValueResolver;
import ghost.framework.web.context.utils.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * package: ghost.framework.web.module.http.request.method.returnValue
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:默认请求函数返回值json解析器，  {@link MediaType#APPLICATION_JSON_VALUE}格式解析器
 * @Date: 2020/3/3:11:01
 */
@HandlerMethodReturnValueResolver
public class RequestMethodReturnValueApplicationJsonResolver
        extends AbstractRequestMethodReturnValueResolver {
    /**
     * 获取是否为默认解析器
     * @return
     */
    @Override
    public boolean isDefault() {
        return def;
    }

    /**
     * 是否为默认解析器
     */
    private boolean def = true;

    /**
     * 设置是否为默认解析器
     * @param def
     */
    @Override
    public void setDefault(boolean def) {
        this.def = def;
    }

    /**
     * 获取响应处理类型
     * 对应 {@link RequestMapping#produces()} 指定类型
     *
     * @return
     */
    @Override
    public String[] getProduces() {
        return produces;
    }

    /**
     * 声明返回处理类型
     */
    private final String[] produces = new String[]{
            MediaType.APPLICATION_JSON_VALUE,
    };

    /**
     *
     * @param request
     * @param response
     * @param requestMethod
     * @param returnValue
     * @throws ReturnValueResolverException
     */
    @Override
    public void handleReturnValue(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                  @NotNull IHttpRequestMethod requestMethod, @NotNull Object returnValue) throws ReturnValueResolverException {
        //返回默认格式 MediaType.APPLICATION_JSON_VALUE
        response.setCharacterEncoding(response.getCharacterEncoding());
        //设置默认返回值解析格式
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + WebUtils.CONTENT_TYPE_CHARSET_PREFIX + response.getCharacterEncoding());
        try {
            response.getOutputStream().write(this.objectMapper.writeValueAsString(returnValue).getBytes(response.getCharacterEncoding()));
            //设置状态码
            if (response.getStatus() <= 0) {
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (JsonProcessingException jpe) {
            //处理json转换错误
            throw new ReturnValueResolverException(jpe.getMessage(), jpe);
        } catch (IOException io) {
            //处理流错误
            throw new ReturnValueResolverException(io.getMessage(), io);
        }
    }

    /**
     * 注入应用序列化
     */
    @Application
    @Autowired
    private ObjectMapper objectMapper;
}