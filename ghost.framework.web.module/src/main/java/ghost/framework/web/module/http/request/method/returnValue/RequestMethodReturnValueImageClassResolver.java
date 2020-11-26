package ghost.framework.web.module.http.request.method.returnValue;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.context.resolver.ReturnValueResolverException;
import ghost.framework.util.FileUtil;
import ghost.framework.web.context.bens.annotation.HandlerMethodReturnValueResolver;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.http.MediaType;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.http.request.method.returnValue.AbstractRequestMethodReturnValueClassResolver;
import ghost.framework.web.context.utils.WebUtils;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * package: ghost.framework.web.module.http.request.method.returnValue
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:返回图片类型解析器
 * 处理注释 {@link RequestMapping#produces::MediaType.IMAGE_GIF_VALUE|MediaType.IMAGE_JPEG_VALUE|MediaType.IMAGE_PNG_VALUE} 返回 {@link BufferedImage}、{@link byte[]}类型返回值解析
 * @Date: 2020/2/29:16:45
 */
@HandlerMethodReturnValueResolver
public class RequestMethodReturnValueImageClassResolver
        extends AbstractRequestMethodReturnValueClassResolver {
    /**
     * 声明解析器支持解析类型
     */
    private final Class<?>[] returnTypes = new Class[]{
            byte[].class,
            BufferedImage.class,
            File.class,
            URL.class
    };

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

    @Override
    public boolean isResolver(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                              @NotNull IHttpRequestMethod requestMethod, @NotNull Object returnValue) {
        return super.isResolver(request, response, requestMethod, returnValue);
    }

    /**
     * 声明返回处理类型
     */
    private final String[] produces = new String[]{
            MediaType.IMAGE_GIF_VALUE,
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE
    };

    /**
     * 获取解析器类型
     *
     * @return
     */
    @Override
    public Class<?>[] getReturnTypes() {
        return returnTypes;
    }

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
        try {
            String extensionName = null;
            //清除响应编码，避免图片头Content-Type参数被带编码格式
            response.setCharacterEncoding(null);
            //获取请求函数注释
            RequestMapping requestMapping = null;
            //判断是否由请求注释
            if (requestMethod.getMethod().isAnnotationPresent(RequestMapping.class)) {
                requestMapping = requestMethod.getMethod().getAnnotation(RequestMapping.class);
            }
            //获取注释响应类型
            if (request.getAttribute(WebUtils.IMAGE_TYPE_ATTRIBUTE) == null) {
                if (requestMapping == null) {
                    //默认为jpeg类型
                    response.setHeader("Content-Type", MediaType.IMAGE_JPEG_VALUE);
                } else {
                    if (requestMapping.produces().length > 0) {
                        response.setHeader("Content-Type", requestMapping.produces()[0]);
                    } else {
                        //默认为jpeg类型
                        response.setHeader("Content-Type", MediaType.IMAGE_JPEG_VALUE);
                    }
                }
            } else {
                //设置自定义格式
                response.setHeader("Content-Type", request.getAttribute(WebUtils.IMAGE_TYPE_ATTRIBUTE).toString());
            }
            //获取文件扩展名
            extensionName = StringUtils.split(response.getHeader("Content-Type"), "/")[1];
            if (returnValue instanceof BufferedImage) {
                BufferedImage image = (BufferedImage) returnValue;
                ImageIO.write(image, extensionName, response.getOutputStream());
            }
            if (returnValue instanceof File) {
                File file = (File) returnValue;
                if (!StringUtils.isEmpty(FileUtil.getExtensionName(file.getAbsolutePath()))) {
                    extensionName = FileUtil.getExtensionName(file.getAbsolutePath());
                    response.setHeader("Content-Type", "image/" + extensionName);
                }
                ImageIO.write(ImageIO.read(file), extensionName, response.getOutputStream());
            }
            if (returnValue instanceof URL) {
                URL url = (URL) returnValue;
                BufferedImage image = ImageIO.read(url);
                if (!StringUtils.isEmpty(FileUtil.getExtensionName(url.getFile()))) {
                    extensionName = FileUtil.getExtensionName(url.getFile());
                    //按照图片类型
                    response.setHeader("Content-Type", "image/" + extensionName);
                }
                ImageIO.write(image, extensionName, response.getOutputStream());
            }
            if (returnValue.getClass().equals(byte[].class)) {
                response.getOutputStream().write((byte[]) returnValue);
            }
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException io) {
            throw new ReturnValueResolverException(io.getMessage(), io);
        }
    }
}