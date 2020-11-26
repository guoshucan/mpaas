package ghost.framework.tomcat.web.module;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.http.impl.client.ProxyClient;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * package: ghost.framework.tomcat.web.module
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 1:23 2020/1/29
 */
public class TomcatTest {
    public static void main(String[] args) throws Exception {
       String servletContext = "";
        // start server
//        EchoServer echoServer = new EchoServer(SERVER_HTTP_PORT, false);
        // wait for server to start up
        TimeUnit.MILLISECONDS.sleep(500);
        // start proxy (in tomcat)
        Tomcat tomcat = new Tomcat();
        File file = new File(System.getProperty("java.io.tmpdir") + File.separator + "8080");
        if(file.exists()){
//            file.delete();
        }
        file.mkdirs();
//        file.deleteOnExit();
        tomcat.setBaseDir(file.getAbsolutePath());
        // add http port
        tomcat.setPort(8000);
        // add servlet
        Context ctx = tomcat.addContext("/" + servletContext, file.getAbsolutePath());
//        tomcat.addServlet("/" + servletContext, "mockServerServlet", new Http2Servlet3());
        ctx.addServletMappingDecoded("/*", "mockServerServlet");

        // start server
        tomcat.start();
        tomcat.getServer().await();
        // start client
//        proxyClient = new ProxyClient("localhost", PROXY_PORT, servletContext);
    }
}
