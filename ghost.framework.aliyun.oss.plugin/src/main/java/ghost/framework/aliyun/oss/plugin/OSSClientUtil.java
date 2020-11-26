//
//package ghost.framework.aliyun.oss.plugin;
//
//import com.aliyun.oss.OSSClient;
//import com.aliyun.oss.entity.ObjectMetadata;
//import com.aliyun.oss.entity.PutObjectResult;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import java.io.InputStream;
//import java.net.URL;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Random;
//public class OSSClientUtil {
//    protected final Log logger = LogFactory.getLog(OSSClientUtil.class);
//    private static String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
//    private static String accessKeyId = "";
//    private static String accessKeySecret = "";
//    private static String filedirImage = "image/";
//    private static String filedirVideo = "video/";
//    private static String filedirForGift = "Images/";
//    //private static String bucketname;
//    private static OSSClient ossClient;
//    public static void init()
//    {
//        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
//    }
//    public static void destory()
//    {
//        ossClient.shutdown();
//    }
//    /**
//     * 图片上传到阿里云
//     * @param instream
//     * @return
//     */
//    public  static HashMap<String, Object> uploadImg2Oss(InputStream instream, String name, String bucketname) {
//        //InputStream inputStream=null;
//        String fileName ;
//        HashMap<String, Object> oosObject = new HashMap<>();
//        String objectname = "";
//        try
//        {
//            if(instream == null ) throw new Exception("图片上传失败");
//
//            init();
//
//            ObjectMetadata objectMetadata = new ObjectMetadata();
//            objectMetadata.setContentLength(instream.available());
//            objectMetadata.setCacheControl("no-cache");
//
//            objectMetadata.setHeader("Pragma", "no-cache");
//            objectMetadata.setContentType(getcontentType(name.substring(name.lastIndexOf("."))));
//            objectMetadata.setContentDisposition((new StringBuilder("inline;filename=")).append(name).toString());
//
//
//            PutObjectResult putObjectResult = ossClient.putObject(bucketname, filedirImage+name, instream, objectMetadata);
//            Date expiration = new Date(new Date().getTime() + 3600 * 1000 * 24 * 600 * 1000 * 1000);
//            fileName = putObjectResult.getETag();
//            objectname = filedirImage+name;
//            URL url = ossClient.generatePresignedUrl(bucketname, objectname, expiration);
//            oosObject.put("url",url);
//            oosObject.put("fileName",fileName);
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//            throw new RuntimeException("图片上传失败");
//        }finally{
//            try {
//                if (instream != null) {
//                    instream.close();
//                }
//                ossClient.shutdown();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return oosObject;
//    }
//
//
//
//    //删除阿里云视频
//
//
//
//    /**
//     * 展示视频上传到阿里云
//     * @param instream
//     * @param name
//     * @return
//     */
//    public  static HashMap<String, Object> uploadVideo2Oss(InputStream instream, String name,String bucketname) {
//        String fileName = null;
//        HashMap<String, Object> oosObject = new HashMap<>();
//        String objectname = "";
//        try
//        {
//            if(instream == null)
//                throw new Exception("视频上传失败");
//
//            init();
//
//            ObjectMetadata objectMetadata = new ObjectMetadata();
//            objectMetadata.setContentLength(instream.available());
//            objectMetadata.setCacheControl("no-cache");
//            objectMetadata.setHeader("Pragma", "no-cache");
//            String substring = name.substring(name.lastIndexOf("."));
//            objectMetadata.setContentType(name.substring(name.lastIndexOf(".")));
//            objectMetadata.setContentDisposition((new StringBuilder("inline;filename=")).append(name).toString());
//
//            PutObjectResult putObjectResult = ossClient.putObject(bucketname, filedirVideo+name, instream, objectMetadata);
//            //eTag = putObjectResult.getETag();
//            Date expiration = new Date(new Date().getTime() + 3600 * 1000 * 24 * 60 * 10);
//            fileName = putObjectResult.getETag();
//            objectname = filedirVideo+name;
//            URL url = ossClient.generatePresignedUrl(bucketname, objectname, expiration);
//            oosObject.put("url",url);
//            oosObject.put("fileName",fileName);
//        } catch(Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("视频上传失败");
//        }finally{
//            try {
//                if (instream != null) {
//                    instream.close();
//                }
//                ossClient.shutdown();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return oosObject;
//    }
//
//
//    /**
//     * 视频上传到阿里云
//     * @param file
//     * @return
//     */
///*    public static String uploadVideo2Oss(MultipartFile file)
//    {
//        try
//        {
//            String originalFilename = file.getOriginalFilename();
//            String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
//            Random random = new Random();
//            String name = (new StringBuilder(String.valueOf((long)random.nextInt(10000) + System.currentTimeMillis()))).append(substring).toString();
//            InputStream inputStream = file.getInputStream();
//            ObjectMetadata objectMetadata = new ObjectMetadata();
//            objectMetadata.setContentLength(inputStream.available());
//            objectMetadata.setContentType("video/mp4");
//            ossClient.putObject(bucketName, (new StringBuilder("video/")).append(name).toString(), inputStream, objectMetadata);
//            inputStream.close();
//            return name;
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//            throw new RuntimeException("视频上传失败");
//        }finally{
//        	 ossClient.shutdown();
//        }
//    }*/
//
//
//
//    public static String uploadImg2Oss(MultipartFile file, String type, String bucketname)
//    {
//        try
//        {
//            String originalFilename = file.getOriginalFilename();
//            String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
//            Random random = new Random();
//            String name = (new StringBuilder(String.valueOf((long)random.nextInt(10000) + System.currentTimeMillis()))).append(substring).toString();
//            InputStream inputStream = file.getInputStream();
//            ObjectMetadata objectMetadata = new ObjectMetadata();
//            objectMetadata.setContentLength(inputStream.available());
//            objectMetadata.setCacheControl("no-cache");
//            objectMetadata.setHeader("Pragma", "no-cache");
//            objectMetadata.setContentType(getcontentType(name.substring(name.lastIndexOf("."))));
//            objectMetadata.setContentDisposition((new StringBuilder("inline;filename=")).append(name).toString());
//            ossClient.putObject(bucketname, (new StringBuilder(String.valueOf(filedirForGift))).append(name).toString(), inputStream, objectMetadata);
//            String url = (new StringBuilder(String.valueOf(endpoint.replaceFirst("http://", (new StringBuilder("http://")).append(bucketname).append(".").toString())))).append("/").append(name).toString();
//            System.out.println(url);
//            System.out.println(name);
//            inputStream.close();
//            return name;
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//            throw new RuntimeException("图片上传失败");
//        }finally{
//        	 ossClient.shutdown();
//        }
//    }
//
//    /**
//     * 获取图片类型
//     * @param FilenameExtension
//     * @return
//     */
//    public static String getcontentType(String FilenameExtension)
//    {
//        if(FilenameExtension.equalsIgnoreCase("bmp"))
//            return "image/bmp";
//        if(FilenameExtension.equalsIgnoreCase("gif"))
//            return "image/gif";
//        if(FilenameExtension.equalsIgnoreCase("jpeg") || FilenameExtension.equalsIgnoreCase("jpg") || FilenameExtension.equalsIgnoreCase("png"))
//            return "image/jpeg";
//        if(FilenameExtension.equalsIgnoreCase("html"))
//            return "text/html";
//        if(FilenameExtension.equalsIgnoreCase("txt"))
//            return "text/plain";
//        if(FilenameExtension.equalsIgnoreCase("vsd"))
//            return "application/vnd.visio";
//        if(FilenameExtension.equalsIgnoreCase("pptx") || FilenameExtension.equalsIgnoreCase("ppt"))
//            return "application/vnd.ms-powerpoint";
//        if(FilenameExtension.equalsIgnoreCase("docx") || FilenameExtension.equalsIgnoreCase("doc"))
//            return "application/msword";
//        if(FilenameExtension.equalsIgnoreCase("xml"))
//            return "text/xml";
//        else
//            return "image/jpeg";
//    }
//}
