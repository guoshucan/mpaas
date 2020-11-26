package ghost.framework.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * package: ghost.framework.context.util
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-16:12:25
 */
public final class YmlUtil {
    public static final String YML = "yml";
    public static final String DotYML = "." + YML;
//    /**
//     * pathYml文件参数合并大盘env中
//     * @param is yml资源文件流
//     * @param env
//     */
//    public static void merge(InputStream is, Environment env) {
//        Yaml yaml = new Yaml();
//        Map<String, Object> map = yaml.loadAs(is, Map.class);
//        for (Map.Entry<String, Object> entry : map.entrySet()) {
//            mergeNext(env, entry);
//        }
//    }
//
//    /**
//     * 获取下个节点配置参数
//     * @param env
//     * @param entry
//     */
//    public static void mergeNext(Environment env, Map.Entry<String, Object> entry) {
//        if(entry.getOrderValue() instanceof String){
//
//        }
//    }
    private static final String ENCODING = "utf-8";
    /**
     * yml转properties
     * @param path
     */
    public static Map<String, String> yml2Properties(String path) {
        final String DOT = ".";
        Map<String, String> lines = new ConcurrentHashMap<>();
        try {
            YAMLFactory yamlFactory = new YAMLFactory();
            YAMLParser parser = yamlFactory.createParser(new InputStreamReader(new FileInputStream(path), Charset.forName(ENCODING)));
            String key = "";
            String value = null;
            JsonToken token = parser.nextToken();
            while (token != null) {
                if (JsonToken.START_OBJECT.equals(token)) {
                    // do nothing
                } else if (JsonToken.FIELD_NAME.equals(token)) {
                    if (key.length() > 0) {
                        key = key + DOT;
                    }
                    key = key + parser.getCurrentName();

                    token = parser.nextToken();
                    if (JsonToken.START_OBJECT.equals(token)) {
                        continue;
                    }
                    value = parser.getText();
                    lines.put(key, value);

                    int dotOffset = key.lastIndexOf(DOT);
                    if (dotOffset > 0) {
                        key = key.substring(0, dotOffset);
                    }
                    value = null;
                } else if (JsonToken.END_OBJECT.equals(token)) {
                    int dotOffset = key.lastIndexOf(DOT);
                    if (dotOffset > 0) {
                        key = key.substring(0, dotOffset);
                    } else {
//                        value = "";
//                        lines.put("");
                    }
                }
                token = parser.nextToken();
            }
            parser.close();
            System.out.println(lines);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return lines;
    }

    /**
     * properties转yml
     * @param path
     */
    public static void properties2Yaml(String path) {
        JsonParser parser = null;
        JavaPropsFactory factory = new JavaPropsFactory();
        try {
            parser = factory.createParser(
                    new InputStreamReader(new FileInputStream(path), Charset.forName(ENCODING)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            YAMLFactory yamlFactory = new YAMLFactory();
            YAMLGenerator generator = yamlFactory.createGenerator(
                    new OutputStreamWriter(new FileOutputStream(path), Charset.forName(ENCODING)));

            JsonToken token = parser.nextToken();

            while (token != null) {
                if (JsonToken.START_OBJECT.equals(token)) {
                    generator.writeStartObject();
                } else if (JsonToken.FIELD_NAME.equals(token)) {
                    generator.writeFieldName(parser.getCurrentName());
                } else if (JsonToken.VALUE_STRING.equals(token)) {
                    generator.writeString(parser.getText());
                } else if (JsonToken.END_OBJECT.equals(token)) {
                    generator.writeEndObject();
                }
                token = parser.nextToken();
            }
            parser.close();
            generator.flush();
            generator.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 获取yml配置文件
     *
     * @param loader 类加载器
     * @param name   资源路径名称
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getMap(ClassLoader loader, String name) throws Exception {
        Map<String, Object> map = null;
        Yaml yaml = new Yaml();
        //文件路径是相对类目录(src/main/java)的相对路径
        try (InputStream in = loader.getResourceAsStream(name)) {
            map = yaml.loadAs(in, Map.class);
        } catch (Exception e) {
            throw e;
        }
        return map;
    }
//    private final static DumperOptions OPTIONS = new DumperOptions();
//    static {
//        //将默认读取的方式设置为块状读取
//        OPTIONS.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
//    }
//    /**
//     * 在目标文件中添加新的配置信息
//     *
//     * @param dest  需要添加信息的目标yml文件
//     * @param value   添加的key值
//     * @param value 添加的对象(如key下方还有链接则添加LinkedHashMap)
//     * @author Relic
//     * @title addIntoYml
//     * @date 2019/1/29 20:52
//     */
//    public static void addIntoYml(File dest, String value, Object value) throws IOException {
//        Yaml yaml = new Yaml(OPTIONS);
//        //载入当前yml文件
//        LinkedHashMap<String, Object> dataMap = yaml.load(new FileReader(dest));
//        //如果yml内容为空,则会引发空指针异常,此处进行判断
//        if (null == dataMap) {
//            dataMap = new LinkedHashMap<>();
//        }
//        dataMap.put(value, value);
//        //将数据重新写回文件
//        yaml.dump(dataMap, new FileWriter(dest));
//    }
//    /**
//     * 从目标yml文件中读取出所指定key的值
//     *
//     * @param source 获取yml信息的文件
//     * @param value    需要获取信息的key值
//     * @return java.lang.Object
//     * @author Relic
//     * @title getFromYml
//     * @date 2019/1/29 20:56
//     */
//    public static Object getFromYml(File source, String value) throws IOException {
//        Yaml yaml = new Yaml(OPTIONS);
//        //载入文件
//        LinkedHashMap<String, Object> dataMap = yaml.load(new FileReader(source));
//        //获取当前key下的值(如果存在多个节点,则value可能为map,自行判断)
//        return dataMap.get(value);
//    }
}