package ghost.framework.io;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 20:39 2018-11-27
 */
public final class ImageUtil {
    /**
     * 获取图片文件格式
     * @param path 图片文件路径或文件名称
     * @return
     */
    public static ImageFormat getImageFormat(String path){
        return ImageFormat.valueOf(path.substring(path.lastIndexOf(".") + 1));
    }
    /**
     * 获取base64图片类型
     * @param format
     * @return
     * @throws IOImageException
     */
    public static String getBase64ImageTypeString(ImageFormat format) throws IOImageException{
        switch (format) {
            case png:
                return "data:image/png;base64";
            case jpg:
                return "data:image/jpg;base64";
            case jpeg:
                return "data:image/jpeg;base64";
            case gif:
                return "data:image/gif;base64";
            case bmp:
                return "data:image/bmp;base64";
            case ico:
                return "data:image/x-icon;base64";
        }
        throw new IOImageException("图片格式错误！");
    }
    /**
     * 压缩图片
     *
     * @param base64 图片数据
     * @param h 高度
     * @param w 宽度
     * @return
     * @throws IOException
     */
    public static String compressedBase64ImageToString(String base64, int h, int w) throws IOException {
        String[] strings = StringUtils.split(base64, ",");
        ImageFormat type = ImageFormat.png;
        switch (strings[0]) {
            case "data:image/png;base64":
                type = ImageFormat.png;
                break;
            case "data:image/jpg;base64":
                type = ImageFormat.jpg;
                break;
            case "data:image/jpeg;base64":
                type = ImageFormat.jpeg;
                break;
            case "data:image/gif;base64":
                type = ImageFormat.gif;
                break;
            case "data:image/bmp;base64":
                type = ImageFormat.bmp;
                break;
            case "data:image/x-icon;base64":
                type = ImageFormat.ico;
                break;
        }
        ByteArrayInputStream bais = null;
        BufferedImage bi1;
        ByteArrayOutputStream baos = null;
        byte[] bytes = null;
        try {
            bais = new ByteArrayInputStream(decoder.decodeBuffer(strings[1]));
            bi1 = ImageIO.read(bais);
            baos = new ByteArrayOutputStream();
            Thumbnails.of(bi1).size(h, w).outputFormat(type.name()).keepAspectRatio(false).toOutputStream(baos);
            bytes = baos.toByteArray();
        } catch (IOException e) {
            throw e;
        } finally {
            if (bais != null) bais.close();
            if (baos != null) baos.close();
        }
        return getBase64ImageTypeString(type) + "," + encoder.encodeBuffer(bytes).trim();
    }
    /**
     * 图片数据转图片base64数据
     * @param bytes 图片数据
     * @param path 图片文件路径
     * @return
     * @throws IOException
     */
    public static String bytesImageToBase64String(byte[] bytes, String path) throws IOException {
        return bytesImageToBase64String(bytes, ImageFormat.valueOf(FileUtil.getExtensionName(path).replace(".", "")));
    }
    /**
     * 图片数据转图片base64数据
     * @param bytes 图片数据
     * @param format 图片格式
     * @return
     * @throws IOException
     */
    public static String bytesImageToBase64String(byte[] bytes, ImageFormat format) throws IOException {
        ByteArrayInputStream bais = null;
        ByteArrayOutputStream baos = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            BufferedImage bi = ImageIO.read(bais);
            baos = new ByteArrayOutputStream();
            ImageIO.write(bi, format.name(), baos);
            return encoder.encodeBuffer(baos.toByteArray()).trim();
        } finally {
            if (bais != null) bais.close();
            if (baos != null) baos.close();
        }
    }
    /**
     * 压缩图片
     * @param bytes 图片原始数据
     * @param h 图片高度
     * @param w 图片宽度
     * @param path 文件路径
     * @return
     * @throws IOException
     */
    public static String compressedImageToBase64String(byte[] bytes, int h, int w, String path) throws IOException {
        return compressedImageToBase64String(bytes, h, w, ImageFormat.valueOf(FileUtil.getExtensionName(path).replace(".", "")));
    }
    /**
     * 压缩图片
     * @param bytes 图片原始数据
     * @param h 图片高度
     * @param w 图片宽度
     * @param format 图片格式
     * @return
     * @throws IOException
     */
    public static String compressedImageToBase64String(byte[] bytes, int h, int w, ImageFormat format) throws IOException {
        ByteArrayInputStream bais = null;
        BufferedImage bi1;
        ByteArrayOutputStream baos = null;
        byte[] b = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            bi1 = ImageIO.read(bais);
            baos = new ByteArrayOutputStream();
            Thumbnails.of(bi1).size(h, w).outputFormat(format.name()).keepAspectRatio(false).toOutputStream(baos);
            b = baos.toByteArray();
        } catch (IOException e) {
            throw e;
        } finally {
            if (bais != null) bais.close();
            if (baos != null) baos.close();
        }
        return (getBase64ImageTypeString(format) + "," + encoder.encode(b));//.getBytes("UTF-8");
    }
    /**
     * 验证是否为base64图片内容。
     *
     * @param base64 图片数据。
     * @param formats 指定验证类型。
     * @return
     */
    public static boolean isBase64Image(String base64, ImageFormat[] formats) {
        String[] strings = StringUtils.split(base64, ",");
        for (ImageFormat format : formats) {
            if (format == ImageFormat.png && strings[0].equals("data:image/png;base64")) {
                return true;
            }
            if (format == ImageFormat.jpg && strings[0].equals("data:image/jpg;base64")) {
                return true;
            }
            if (format == ImageFormat.jpeg && strings[0].equals("data:image/jpeg;base64")) {
                return true;
            }
            if (format == ImageFormat.gif && strings[0].equals("data:image/gif;base64")) {
                return true;
            }
            if (format == ImageFormat.ico && strings[0].equals("data:image/x-icon;base64")) {
                return true;
            }
            if (format == ImageFormat.bmp && strings[0].equals("data:image/bmp;base64")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证是否为base64图片内容。
     *
     * @param base64 图片数据。
     * @return
     */
    public static boolean isBase64Image(String base64) {
        String[] strings = StringUtils.split(base64, ",");
        switch (strings[0]) {
            case "data:image/png;base64":
                return true;
            case "data:image/jpg;base64":
                return true;
            case "data:image/jpeg;base64":
                return true;
            case "data:image/gif;base64":
                return true;
            case "data:image/x-icon;base64":
                return true;
            case "data:image/bmp;base64":
                return true;
        }
        return false;
    }
    private static BASE64Encoder encoder = new BASE64Encoder();
    private static BASE64Decoder decoder = new BASE64Decoder();

//    /**
//     * 图片流转base63的byte[]格式
//     * @param stream 图片流
//     * @param suffix 流图片文件扩展名
//     * @return
//     * @throws Exception
//     */
//    public static byte[] streamToBase64Bytes(InputStream stream, String suffix) throws Exception{
//        BufferedImage bi1;
//        ByteArrayOutputStream baos = null;
//        byte[] bytes = null;
//        try {
//            bi1 = ImageIO.read(stream);
//            baos = new ByteArrayOutputStream();
//
//            Thumbnails.of(bi1).outputFormat(suffix.toLowerCase()).keepAspectRatio(false).toOutputStream(baos);
//            bytes = baos.toByteArray();
//        } catch (IOException e) {
//            throw e;
//        } finally {
//            if (baos != null) baos.close();
//        }
//        return bytes;
//    }
}
