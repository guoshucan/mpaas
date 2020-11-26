package ghost.framework.web.mvc.freemarker.plugin;

import freemarker.cache.CacheStorage;
import freemarker.cache.TemplateLoader;
import freemarker.template.Version;
import ghost.framework.beans.annotation.bean.Bean;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.context.io.IResourceDomain;
import ghost.framework.web.context.io.WebIResourceLoader;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * package: ghost.framework.web.mvc.freemarker.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/7:21:12
 */
@Configuration()
public class FreeMarkerConfig {
    freemarker.template.Configuration configuration = new freemarker.template.Configuration(new Version("2.3.30"));
    private CacheStorage cacheStorage = new CacheStorage() {
        private Map<Object, Object> map = new HashMap<>();

        @Override
        public Object get(Object key) {
            return map.get(key);
        }

        @Override
        public void put(Object key, Object value) {
            map.put(key, value);
        }

        @Override
        public void remove(Object key) {
            map.remove(key);
        }

        @Override
        public void clear() {
            map.clear();
        }
    };
    /**
     * web资源加载器接口
     */
    @Autowired
    private WebIResourceLoader<IResourceDomain> resourceLoader;
    /**
     * 模板加载器
     */
    private TemplateLoader templateLoader = new TemplateLoader() {

        @Override
        public Object findTemplateSource(String name) throws IOException {
            return null;
        }

        @Override
        public long getLastModified(Object templateSource) {
            return 0;
        }

        @Override
        public Reader getReader(Object templateSource, String encoding) throws IOException {
            return null;
        }

        @Override
        public void closeTemplateSource(Object templateSource) throws IOException {

        }
    };

    @Bean
    public freemarker.template.Configuration configuration() {
        configuration.setCacheStorage(cacheStorage);
        configuration.setTemplateLoader(templateLoader);
        return configuration;
    }
}