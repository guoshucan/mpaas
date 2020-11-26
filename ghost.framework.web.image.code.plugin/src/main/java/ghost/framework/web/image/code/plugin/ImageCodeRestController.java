package ghost.framework.web.image.code.plugin;

import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.bind.annotation.RestController;
import ghost.framework.web.context.http.responseContent.DataResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * package: ghost.framework.web.image.code.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:图片验证码控制器
 * @Date: 2020/3/10:16:28
 */
@RestController(value = "/api/code", proxy = false)
public final class ImageCodeRestController {
    /**
     * 验证码键
     */
    private final String VERIFICATION_CODE_ATTRIBUTE = ImageCodeRestController.class.getName() + "_code";

    /**
     * 获取图片验证码
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("6c9028a3-2ad0-47ea-8fc2-9e3f905a141f")
    public void getVerificationCode(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {
        /*
             1.生成验证码
             2.把验证码上的文本存在session中
             3.把验证码图片发送给客户端
             */
        ImageCode code = new ImageCode();     //用我们的验证码类，生成验证码类对象
        BufferedImage image = code.getImage();  //获取验证码
        request.getSession().setAttribute(VERIFICATION_CODE_ATTRIBUTE, code.getText()); //将验证码的文本存在session中
        code.output(image, response.getOutputStream());//将验证码图片响应给客户端
    }

    /**
     * 获取验证码
     *
     * @param request
     * @return
     */
    @RequestMapping("af1a8ad3-bd9d-427f-8d89-89fa77d036b0")
    public DataResponse verificationCode(
            HttpServletRequest request) {
        DataResponse dr = new DataResponse();
        String code = (String) request.getSession().getAttribute(VERIFICATION_CODE_ATTRIBUTE);    //从session中获取真正的验证码
        dr.setData(code);
        return dr;
    }
}