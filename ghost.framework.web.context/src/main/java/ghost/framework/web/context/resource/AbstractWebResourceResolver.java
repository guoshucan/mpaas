package ghost.framework.web.context.resource;

import ghost.framework.util.FileUtil;
import ghost.framework.web.context.http.HttpHeaders;
import ghost.framework.web.context.http.HttpMethod;
import ghost.framework.web.context.io.IWebResource;
import ghost.framework.web.context.server.MimeMappings;
import ghost.framework.web.context.utils.DateUtils;
import ghost.framework.web.context.utils.ETag;
import ghost.framework.web.context.utils.ETagUtils;
import ghost.framework.web.context.utils.WebUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * package: ghost.framework.web.module.http.response
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:响应资源解析器基础类
 * @Date: 2020/3/14:23:48
 */
public abstract class AbstractWebResourceResolver implements IWebResourceResolver {
    protected final Log logger = LogFactory.getLog(getClass());

    @Override
    public void resolver(String path, HttpServletRequest request, HttpServletResponse response, final IWebResource resource) throws IOException {
        //获取资源的etag对象
        final ETag etag = resource.getETag();
        //获取资源文件修改时间
        final Date lastModified = new Date(resource.lastModified());
        //处理文件缓存
        if (!ETagUtils.handleIfMatch(request.getHeader(HttpHeaders.IF_MATCH), etag, false) ||
                !DateUtils.handleIfUnmodifiedSince(request.getHeader(HttpHeaders.IF_UNMODIFIED_SINCE), lastModified)) {
            response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
            return;
        }
        if (!ETagUtils.handleIfNoneMatch(request.getHeader(HttpHeaders.IF_NONE_MATCH), etag, true) ||
                !DateUtils.handleIfModifiedSince(request.getHeader(HttpHeaders.IF_MODIFIED_SINCE), lastModified)) {
            if (request.getMethod().equals(HttpMethod.GET.name()) || request.getMethod().equals(HttpMethod.HEAD.name())) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            } else {
                response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
            }
            return;
        }
        //判断处理响应内容类型
        if (response.getContentType() == null) {
            if (!resource.isDirectory()) {
                //获取类型地图
                MimeMappings mimeMappings = (MimeMappings) request.getAttribute(WebUtils.MIME_MAPPINGS_ATTRIBUTE);
                //获取资源内容类型
                final String contentType = mimeMappings.get(FileUtil.getExtensionName(resource.getPath()));
                if (contentType != null) {
                    response.setContentType(contentType);
                } else {
                    response.setContentType("application/octet-stream");
                }
            }
        }
        //判断资源是否有修改时间
        if (lastModified != null) {
            //设置文件修改时间
            response.setHeader(HttpHeaders.LAST_MODIFIED, resource.getLastModifiedString());
        }
        if (etag != null) {
            response.setHeader(HttpHeaders.ETAG, etag.toString());
        }

        //资源文件大小
        long downloadSize = resource.contentLength();
        Long fromPos = new Long(0);
        Long toPos = new Long(0);
        if (request.getHeader("Range") == null) {
            //一次性读取长度
            response.setHeader("Content-Length", downloadSize + "");
            response.getOutputStream().write(resource.getBytes());
            response.flushBuffer();
            return;
        } else {
            //处理断点续传支持
            // set headers for the response
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", resource.getFilename());
            response.setHeader(headerKey, headerValue);
            // 解析断点续传相关信息
            response.setHeader("Accept-Ranges", "bytes");
            // 若客户端传来Range，说明之前下载了一部分，设置206状态(SC_PARTIAL_CONTENT)
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            String range = request.getHeader("Range");
            String bytes = range.replaceAll("bytes=", "");
            String[] ary = bytes.split("-");
            //读取断点开始位置
            fromPos = Long.parseLong(ary[0]);
            if (ary.length == 2) {
                //读取断点结束位置
                toPos = Long.parseLong(ary[1]);
            }
            //读取的段大小
            int size;
            //判断读取段大小
            if (toPos > fromPos) {
                size = (int) (toPos - fromPos);
            } else {
                size = (int) (downloadSize - fromPos);
            }
            //分段读取长度
            response.setHeader("Content-Length", size + "");
            //重置分段大小
            downloadSize = size;
        }
        //分块读取
        OutputStream out;
        try (ByteArrayInputStream stream = new ByteArrayInputStream(resource.getBytes())) {
            // 缓冲区大小
            byte[] buffer = new byte[Long.valueOf(downloadSize < 2048 ? downloadSize : 2048).intValue()];
            //读取数量
            int num = 0;
            //计数器
            int count = 0;
            out = response.getOutputStream();
            //设置流读取位置，这个主要为分段读取定位流位置，如果不为分段下载为0位置
            if (fromPos > 0) {
                stream.skip(fromPos.intValue());
            }
            //循环读取流
            while ((num = stream.read(buffer, 0, buffer.length)) != -1) {
                //写入流
                out.write(buffer, 0, num);
                //累加读取计数
                count += num;
                //处理最后一段，计算不满缓冲区的大小
                if (count >= downloadSize) {
                    break;
                }
            }
            response.flushBuffer();
        } catch (IOException e) {
            logger.info("数据被暂停或中断。");
            throw e;
        }
    }
}