package ghost.framework.jetty.ws.module;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
/**
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 4:31 2019/11/8
 */
public final class WsListener implements WebSocketListener {
    private Session session;
    //发送String
    @Override
    public void onWebSocketText(String message) {
        System.out.println("onWebSocketText");
        if (session.isOpen()) {
            // echo the message back
            session.getRemote().sendString("echo msg->m" + message, null);
        }
    }

    //发送byte[]
    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
        System.out.println("onWebSocketBinary");
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        System.out.println("Error->" + cause.getMessage());
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        System.out.println("onWebSocketClose");
        this.session = null;
    }

    @Override
    public void onWebSocketConnect(org.eclipse.jetty.websocket.api.Session session) {
        System.out.println("onWebSocketConnect->" + session.getRemoteAddress());
        this.session = session;
    }
}