package ghost.framework.core.parser.locale.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.container.BeanListContainer;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.locale.ILocaleContainer;
import ghost.framework.core.locale.LocaleException;
import ghost.framework.core.parser.locale.ILocaleParser;
import ghost.framework.core.parser.locale.ILocaleParserContainer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.misc.IOUtils;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
/**
 * package: ghost.framework.core.parser.locale.json
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:区域化json解析器
 * @Date: 2020/1/21:22:06
 */
@Component
@BeanListContainer(ILocaleParserContainer.class)
public class LocaleJsonParser implements ILocaleParser {
    /**
     * 日志
     */
     private Log log = LogFactory.getLog(LocaleJsonParser.class);
    /**
     * 应用接口
     */
    @Application
    @Autowired
    private IApplication app;

    /**
     * 加载区域资源信息
     *
     * @param url          包url
     * @param container    语言容器
     * @param resourceName 语言资源目录名称
     * @param dev 拥有者是否为开发模式
     */
    @Override
    public void loader(URL url, ILocaleContainer container, String resourceName, boolean dev) {
        //获取json序列化对象
        ObjectMapper objectMapper = this.app.getBean(ObjectMapper.class);
        //判断语言资源路径尾部符号
        String resource;
        //遍历资源所在包文件，查找语言文件
        if (dev/*AssemblyUtils.isUrlDev(url)*/) {
            //开发目录模式
            try {
                if (resourceName.endsWith(File.separator)) {
                    resource = File.separator + resourceName.replaceAll(File.separator, "");
                } else {
                    resource = File.separator + resourceName;
                }
                for (File file : new File(url.getFile()).listFiles()) {
                    if(file.getPath().endsWith(resource)) {
                        System.out.println(file.toPath());
                        for (File file1 : file.listFiles()) {
                            if(file1.getPath().endsWith(".json")) {
                                this.log.debug(file1.toString());
                                Map m = objectMapper.readValue(new String(FileUtils.readFileToByteArray(file1), "UTF-8"), Map.class);
                                container.add(file1.getName().replace(resource, "").replace("-", "_").replace(".json", "").toLowerCase(), m);
                            }
                        }
                        return;
                    }
                }
            } catch (Exception e) {
                throw new LocaleException(resourceName, e);
            }
        } else {
            //包模式
            try (JarFile file = new JarFile(url.getFile())) {
                if (resourceName.endsWith("/")) {
                    resource = resourceName;
                } else {
                    resource = resourceName + "/";
                }
                Enumeration<JarEntry> enumeration = file.entries();
                while (enumeration.hasMoreElements()) {
                    JarEntry entry = enumeration.nextElement();
                    String name = entry.getName().toLowerCase();
                    if (name.startsWith(resource) && name.endsWith(".json")) {
                        this.log.debug(entry.toString());
                        try (InputStream stream = file.getInputStream(entry)) {
                            this.log.debug(stream.toString());
                            Map m = objectMapper.readValue(new String(IOUtils.readNBytes(stream, stream.available()), "UTF-8"), Map.class);
                            container.add(name.replace(resource, "").replace("-", "_").replace(".json", "").toLowerCase(), m);
                        } catch (Exception e) {
                            throw new LocaleException(resourceName, e);
                        }
                    }
                }
            } catch (Exception e) {
                throw new LocaleException(resourceName, e);
            }
        }
    }
}