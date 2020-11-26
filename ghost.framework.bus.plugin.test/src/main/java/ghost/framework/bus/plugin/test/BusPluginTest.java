package ghost.framework.bus.plugin.test;

import ghost.framework.bus.client.plugin.BusClient;
import ghost.framework.bus.server.plugin.BusServer;

/**
 * package: ghost.framework.bus.plugin.test
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/13:10:07
 */
public class BusPluginTest {
    public static void main(String[] args) {
        //
        BusServer server = new BusServer(true, false);
        server.start();
        //
        BusClient client = new BusClient(true);
        client.start();
    }
}
