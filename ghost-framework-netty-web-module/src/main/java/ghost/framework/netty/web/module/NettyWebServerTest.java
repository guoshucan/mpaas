package ghost.framework.netty.web.module;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;

/**
 * package: ghost.framework.netty.web.module
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2:07 2020/1/26
 */
public class NettyWebServerTest {


    static class CompressionDetectionHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            if (msg instanceof HttpResponse) {
                HttpResponse response = (HttpResponse) msg;
                boolean compressed = response.headers().contains(HttpHeaderNames.CONTENT_ENCODING, "gzip", true);
                if (compressed) {
                    response.headers().set("X-Test-Compressed", "true");
                }
            }
            ctx.fireChannelRead(msg);
        }

    }
}
