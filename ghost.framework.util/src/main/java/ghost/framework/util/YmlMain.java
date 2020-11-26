package ghost.framework.util;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

/**
 * package: ghost.framework.context.util
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-16:12:27
 */
public class YmlMain {
    public static void main(String[] args) {
        Yaml yaml = new Yaml();
        //文件路径是相对类目录(src/main/java)的相对路径
        InputStream in = YmlMain.class.getClassLoader().getResourceAsStream("com/zrun/TestReadYamlFile/app.yaml");//或者app.yaml
        Map<String, Object> map = yaml.loadAs(in, Map.class);
        String appid = map.getOrDefault("appid", "123").toString();
        System.out.println(appid);
        String port = ((Map<String, Object>) map.get("server")).get("port").toString();
        System.out.println(port);
    }
}
