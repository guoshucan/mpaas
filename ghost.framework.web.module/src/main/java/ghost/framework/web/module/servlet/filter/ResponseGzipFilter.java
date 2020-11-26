package ghost.framework.web.module.servlet.filter;

import ghost.framework.beans.annotation.conditional.ConditionalOnMissingClass;
import ghost.framework.beans.annotation.container.BeanMapContainer;
import ghost.framework.beans.annotation.order.Order;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.web.context.servlet.context.IFilterContainer;
import ghost.framework.web.context.servlet.filter.GenericFilter;
import ghost.framework.web.context.servlet.filter.IResponseGzipFilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * package: ghost.framework.web.module.servlet.filter
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:GZIP压缩过滤器
 * @Date: 2020/3/5:0:21
 */
@ConditionalOnMissingClass(FilterContainer.class)
@BeanMapContainer(IFilterContainer.class)
@Component
@Order(-1)//执行顺序，小为先
@WebFilter(filterName = "GZIPFilter", urlPatterns = "/*", displayName = "GZIPFilter", description = "", dispatcherTypes = DispatcherType.REQUEST)
public class ResponseGzipFilter extends GenericFilter implements IResponseGzipFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //在输出到浏览器之前将输过来的buffer进行压缩
//        GzipHttpServletResponseWrapper servletResponse = new GzipHttpServletResponseWrapper(response);
        //
        chain.doFilter(request, response);
        //增加getBuffer方法
//        byte[] bytes = servletResponse.getBuffer();
        //输出压缩前的长度以作对比
//        System.out.println("压缩前" + bytes.length);
//        //用GZIPOutputStream压缩
//        try (ByteArrayOutputStream bout = new ByteArrayOutputStream()) {
//            try (GZIPOutputStream gzip = new GZIPOutputStream(bout)) {
//                gzip.write(bytes);
//                //一般输出都要flush
//                gzip.flush();
//            }
//            bytes = bout.toByteArray();
//        }
//        //压缩后
//        System.out.println("压缩后" + bytes.length);
//        //注明压缩类型
//        response.setHeader("content-encoding", "gzip");
//        response.setHeader("content-length", bytes.length + "");
//        //写到浏览器，浏览器会自动解压，只要注明了类型
//        response.getOutputStream().write(bytes);
    }
    /**
     * package: ghost.framework.web.module.servlet
     *
     * @Author: 郭树灿{guo-w541}
     * @link: 手机:13715848993, QQ 27048384
     * @Description:
     * @Date: 2020/3/5:0:23
     */
     class GzipHttpServletResponseWrapper extends HttpServletResponseWrapper {
        private HttpServletResponse response;
        private ByteArrayOutputStream bout = new ByteArrayOutputStream();

        public GzipHttpServletResponseWrapper(HttpServletResponse response) {
            super(response);
            this.response = response;
        }

        /*综合考虑下还是重写getOutStream
        但是需求是要输出到缓存，此方法不适合，于是写一个拓展类类来迎合需求*/
        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return new GzipServletOutputStream(response, bout);
        }

        //将数据封装成一个数组然后发送到缓存
        class GzipServletOutputStream extends ServletOutputStream {
            private HttpServletResponse response;
            private ByteArrayOutputStream bout;

            public GzipServletOutputStream(HttpServletResponse response, ByteArrayOutputStream bout) {
                this.response = response;
                this.bout = bout;
            }

            /*此处应该要将数据包装成一个数组但是
                此处不符合需求，于是重载*/
            @Override
            public void write(int b) throws IOException {

            }

            public void write(byte[] bytes) throws IOException {
                bout.write(bytes);
                bout.flush();
                bout.close();
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {

            }
        }

        public byte[] getBuffer() {
            return bout.toByteArray();
        }
    }
}