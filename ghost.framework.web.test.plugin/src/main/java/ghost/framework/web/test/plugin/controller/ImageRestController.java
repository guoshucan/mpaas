package ghost.framework.web.test.plugin.controller;

import ghost.framework.beans.annotation.injection.TempDirectory;
import ghost.framework.beans.annotation.module.Module;
import ghost.framework.util.FileUtil;
import ghost.framework.web.context.bind.annotation.*;
import ghost.framework.web.context.http.MediaType;
import ghost.framework.web.context.http.multipart.MultipartFile;
import ghost.framework.web.context.utils.WebUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

/**
 * package: ghost.framework.web.test.plugin.controller
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:图片测试控制器
 * @Date: 2020/2/3:22:52
 */
@RestController("/image")
public class ImageRestController {
    /**
     * 上传图片
     * @param file
     * @return
     */
    @RequestMapping(value = "postImage")
    public String postImage(@RequestParam("file") MultipartFile file) {
        //获取上传文件名,包含后缀
        String originalFilename = file.getOriginalFilename();
        //保存路径
        //生成保存文件
        File uploadFile = new File(getModuleTempTest() + File.separator + originalFilename);
        System.out.println(uploadFile);
        //将上传文件保存到路径
        try {
            file.transferTo(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传" + originalFilename + "成功";
    }
    /**
     * 读取图片流
     *
     * @param filename 图片文件名称包括扩展名
     * @param request
     * @return 返回文件缓冲对象，由{@link RequestMapping#produces()} 指定格式解析
     */
    @RequestMapping(
            value = "/readBufferedImage/{filename:.+}",
            method = RequestMethod.GET,
            headers = {},
            //处理响应产品格式
            produces = {
                    MediaType.IMAGE_JPEG_VALUE,
                    MediaType.IMAGE_GIF_VALUE,
                    MediaType.IMAGE_PNG_VALUE
            })
    public BufferedImage readBufferedImage(@PathVariable String filename, HttpServletRequest request) {
        System.out.println("filename:" + filename + "=" + FileUtil.getExtensionName(filename));
        //设置给解析器处理格式类型
        switch (FileUtil.getExtensionName(filename)) {
            case "png":
                request.setAttribute(WebUtils.IMAGE_TYPE_ATTRIBUTE, MediaType.IMAGE_PNG_VALUE);
                break;
            case "gif":
                request.setAttribute(WebUtils.IMAGE_TYPE_ATTRIBUTE, MediaType.IMAGE_GIF_VALUE);
                break;
            case "jpg":
                request.setAttribute(WebUtils.IMAGE_TYPE_ATTRIBUTE, MediaType.IMAGE_JPEG_VALUE);
                break;
            case "jpeg":
                request.setAttribute(WebUtils.IMAGE_TYPE_ATTRIBUTE, MediaType.IMAGE_JPEG_VALUE);
                break;
        }
        try (InputStream stream = getClass().getResourceAsStream("/static/" + filename)) {
            return ImageIO.read(stream);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 注入web模块临时目录
     */
    @Module("ghost.framework.web.module")
    @TempDirectory
    private File moduleTemp;

    /**
     * 获取模块临时测试目录
     *
     * @return
     */
    private File getModuleTempTest() {
        File file = new File(moduleTemp.getPath() + File.separator + "test");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
    /**
     * 读取文件图片
     *
     * @param filename 图片文件名称包括扩展名
     * @param request
     * @return 返回文件缓冲对象，由{@link RequestMapping#produces()} 指定格式解析
     */
    @RequestMapping(
            value = "/readTestFile/{filename:.+}",
            method = RequestMethod.GET,
            headers = {},
            //处理响应产品格式
            produces = {
                    MediaType.IMAGE_JPEG_VALUE,
                    MediaType.IMAGE_GIF_VALUE,
                    MediaType.IMAGE_PNG_VALUE
            })
    public File readTestFile(@PathVariable String filename, HttpServletRequest request) {
        System.out.println("filename:" + filename + "=" + FileUtil.getExtensionName(filename));
        return new File(getModuleTempTest().getPath() + File.separator + filename);
    }

    /**
     * 读取文件图片
     *
     * @param filename 图片文件名称包括扩展名
     * @param request
     * @return 返回文件缓冲对象，由{@link RequestMapping#produces()} 指定格式解析
     */
    @RequestMapping(
            value = "/file/{filename:.+}",
            method = RequestMethod.GET,
            headers = {},
            //处理响应产品格式
            produces = {
                    MediaType.IMAGE_JPEG_VALUE,
                    MediaType.IMAGE_GIF_VALUE,
                    MediaType.IMAGE_PNG_VALUE
            })
    public File readFile(@PathVariable String filename, HttpServletRequest request) {
        System.out.println("filename:" + filename + "=" + FileUtil.getExtensionName(filename));
        try (InputStream stream = getClass().getResourceAsStream("/static/" + filename)) {
            File file = new File(getModuleTempTest().getPath() + File.separator + filename);
            //判断文件夹是否存在
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = stream.read(buf)) != -1) {   //将byte数据读到最多buf长度的buf数组中
                    outputStream.write(buf, 0, len);         //将buf中 从0-len长度的数据写到文件中
                }
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("抛出异常！！");
        }
        return null;
    }

    /**
     * 读取url图片文件
     *
     * @param filename 图片文件名称包括扩展名
     * @param request
     * @return 返回文件缓冲对象，由{@link RequestMapping#produces()} 指定格式解析
     */
    @RequestMapping(
            value = "/url/{filename:.+}",
            method = RequestMethod.GET,
            headers = {},
            //处理响应产品格式
            produces = {
                    MediaType.IMAGE_JPEG_VALUE,
                    MediaType.IMAGE_GIF_VALUE,
                    MediaType.IMAGE_PNG_VALUE
            })
    public URL readURL(@PathVariable String filename, HttpServletRequest request) {
        System.out.println("filename:" + filename + "=" + FileUtil.getExtensionName(filename));
        return getClass().getResource("/static/" + filename);
    }

    /**
     * 读取byte[]读取文件
     *
     * @param filename 图片文件名称包括扩展名
     * @param request
     * @return 返回文件缓冲对象，由{@link RequestMapping#produces()} 指定格式解析
     */
    @RequestMapping(
            value = "/readBuffered/{filename:.+}",
            method = RequestMethod.GET,
            headers = {},
            //处理响应产品格式
            produces = {
                    MediaType.IMAGE_JPEG_VALUE,
                    MediaType.IMAGE_GIF_VALUE,
                    MediaType.IMAGE_PNG_VALUE
            })
    public byte[] readBuffered(@PathVariable String filename, HttpServletRequest request) {
        System.out.println("filename:" + filename + "=" + FileUtil.getExtensionName(filename));
        //设置给解析器处理格式类型
        switch (FileUtil.getExtensionName(filename)) {
            case "png":
                request.setAttribute(WebUtils.IMAGE_TYPE_ATTRIBUTE, MediaType.IMAGE_PNG_VALUE);
                break;
            case "gif":
                request.setAttribute(WebUtils.IMAGE_TYPE_ATTRIBUTE, MediaType.IMAGE_GIF_VALUE);
                break;
            case "jpg":
                request.setAttribute(WebUtils.IMAGE_TYPE_ATTRIBUTE, MediaType.IMAGE_JPEG_VALUE);
                break;
            case "jpeg":
                request.setAttribute(WebUtils.IMAGE_TYPE_ATTRIBUTE, MediaType.IMAGE_JPEG_VALUE);
                break;
        }
        byte[] data = null;
        try (InputStream stream = getClass().getResourceAsStream("/static/" + filename)) {
            try {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int numBytesRead = 0;
                while ((numBytesRead = stream.read(buf)) != -1) {
                    output.write(buf, 0, numBytesRead);
                }
                data = output.toByteArray();
                output.close();
            } catch (FileNotFoundException ex1) {
                ex1.printStackTrace();
            } catch (IOException ex1) {
                ex1.printStackTrace();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return data;
    }
}