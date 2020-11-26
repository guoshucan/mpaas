package ghost.framework.web.test.plugin;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.exception.ExceptionHandler;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.module.Module;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.converter.ConverterException;
import ghost.framework.context.module.IModule;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.bind.annotation.RestControllerAdvice;
import ghost.framework.web.context.bind.annotation.ResponseStatus;
import ghost.framework.web.context.bind.annotation.RestController;
import ghost.framework.web.context.controller.IControllerExceptionHandlerContainer;
import ghost.framework.web.context.http.HttpStatus;
import ghost.framework.web.context.http.MediaType;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * package: ghost.framework.web.module.controller
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:默认控制器 {@link Exception} 错误全局处理
 * 构建Bean后将添加入 {@link IControllerExceptionHandlerContainer} 接口容器中
 * @Date: 2020/2/28:9:20
 */
@RestControllerAdvice(
        //指定拦截包的控制器
//        basePackages = {"ghost.framework.web.module.controller.*"},
        //限定被标注为@Controller或者@RestController的类才被拦截
        annotations = {RestController.class}
)
public class RestControllerHandler {
    private Logger logger = Logger.getLogger(RestControllerHandler.class);

    /**
     * {@link Exception} 错误处理
     *
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(value = Exception.class)//处理未处理错误类型
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)//处理相应状态码
    public Map<String, Object> exception(HttpServletRequest request, Exception ex) {
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("code", HttpStatus.INTERNAL_SERVER_ERROR);
        this.logger.error(ex.getMessage(), ex);
        if (logger.isDebugEnabled()) {
            //调试模式返回错误内容
            msgMap.put("message", ex.getMessage());
        } else {
            msgMap.put("message", "error");
        }
        return msgMap;
    }

    /**
     * {@link ConverterException} 错误处理
     * @param request
     * @param ex
     * @param app
     * @param module
     * @return
     */
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)//注释返回格式，如果未标此注释将按照默认返回格式解析
    @ExceptionHandler(value = ConverterException.class)//处理未处理错误类型
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)//处理相应状态码
    public Map<String, Object> converterException(HttpServletRequest request,
                                                  ConverterException ex,
                                                  @Application @Autowired IApplication app,
                                                  @Autowired @Module("ghost.framework.web.module") IModule module) {
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("code", HttpStatus.INTERNAL_SERVER_ERROR);
        this.logger.error(ex.getMessage(), ex);
        if (logger.isDebugEnabled()) {
            //调试模式返回错误内容
            msgMap.put("message", ex.getMessage());
        } else {
            msgMap.put("message", "converter error");
        }
        return msgMap;
    }
}