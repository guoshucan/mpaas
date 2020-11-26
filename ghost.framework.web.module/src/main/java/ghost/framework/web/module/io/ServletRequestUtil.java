//package ghost.framework.web.module.io;
//
//import ghost.framework.util.StringUtil;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.BufferedReader;
//import java.io.IOException;
//
///**
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 3:45 2019/11/8
// */
//public final class ServletRequestUtil {
//    /**
//     * 读取字符内容
//     *
//     * @param request
//     * @return
//     * @throws IOException
//     */
//    public static String readString(HttpServletRequest request) throws IOException {
//        StringBuilder sb;
//        BufferedReader br = request.getReader();
//        String line = br.readLine();
//        if (line != null) {
//            sb = new StringBuilder();
//            sb.append(line);
//        } else {
//            return "";
//        }
//        while ((line = br.readLine()) != null) {
//            sb.append('\n').append(line);
//        }
//        return sb.toString();
//    }
//
////    public static JsonObject getRequestJsonObject(HttpServletRequest request) throws IOException {
////        String json = getRequestJsonString(request);
////        return new JsonObject().getAsJsonObject(json);
////    }
//
//    /**
//     * 获取get或post的json内容
//     * @param request
//     * @return
//     * @throws IOException
//     */
//    public static String getRequestJsonString(HttpServletRequest request)
//            throws IOException {
//        String submitMehtod = request.getMethod();
//        // GET
//        if (submitMehtod.equals("GET")) {
//            return new String(request.getQueryString().getBytes("iso-8859-1"), StringUtil.UTF8).replaceAll("%22", "\"");
//            // POST
//        } else {
//            return getRequestPostStr(request);
//        }
//    }
//
//    /**
//     * 获取post二进制内容
//     * @param request
//     * @return
//     * @throws IOException
//     */
//    public static byte[] getRequestPostBytes(HttpServletRequest request)
//            throws IOException {
//        int contentLength = request.getContentLength();
//        if (contentLength < 0) {
//            return null;
//        }
//        byte buffer[] = new byte[contentLength];
//        for (int i = 0; i < contentLength; ) {
//
//            int readlen = request.getInputStream().read(buffer, i,
//                    contentLength - i);
//            if (readlen == -1) {
//                break;
//            }
//            i += readlen;
//        }
//        return buffer;
//    }
//
//    /**
//     * 获取post内容
//     * @param request
//     * @return
//     * @throws IOException
//     */
//    public static String getRequestPostStr(HttpServletRequest request)
//            throws IOException {
//        byte buffer[] = getRequestPostBytes(request);
//        String ce = request.getCharacterEncoding();
//        if (ce == null) {
//            ce = StringUtil.UTF8;
//        }
//        return new String(buffer, ce);
//    }
//}