package ghost.framework.data.jdbc.jpa.plugin.repository.config;

/**
 * package: ghost.framework.data.jdbc.jpa.plugin.repository.config
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/7/5:22:31
 */

//import ghost.framework.context.annotation.ContextAnnotationAutowireCandidateResolver;

import ghost.framework.context.bean.DependencyDescriptor;
import ghost.framework.core.annotation.ContextAnnotationAutowireCandidateResolver;
import ghost.framework.data.commons.repository.config.RepositoryConfiguration;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Customer {@link ContextAnnotationAutowireCandidateResolver} that also considers all injection points for lazy
 * repositories lazy.
 *
 * @author Oliver Gierke
 * @since 2.1
 */
public class LazyRepositoryInjectionPointResolver extends ContextAnnotationAutowireCandidateResolver {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(LazyRepositoryInjectionPointResolver.class);
    private final Map<String, RepositoryConfiguration<?>> configurations;

    public LazyRepositoryInjectionPointResolver(Map<String, RepositoryConfiguration<?>> configurations) {
        this.configurations = configurations;
    }

    /**
     * Returns a new {@link LazyRepositoryInjectionPointResolver} that will have its configurations augmented with the
     * given ones.
     *
     * @param configurations must not be {@literal null}.
     * @return
     */
    public LazyRepositoryInjectionPointResolver withAdditionalConfigurations(
            Map<String, RepositoryConfiguration<?>> configurations) {

        Map<String, RepositoryConfiguration<?>> map = new HashMap<>(this.configurations);
        map.putAll(configurations);

        return new LazyRepositoryInjectionPointResolver(map);
    }

    /*
     * (non-Javadoc)
     * @see ghost.framework.context.annotation.ContextAnnotationAutowireCandidateResolver#isLazy(ghost.framework.beans.factory.config.DependencyDescriptor)
     */
    @Override
    protected boolean isLazy(DependencyDescriptor descriptor) {

        Class<?> type = descriptor.getDependencyType();

        RepositoryConfiguration<?> configuration = configurations.get(type.getName());

        if (configuration == null) {
            return super.isLazy(descriptor);
        }

        boolean lazyInit = configuration.isLazyInit();

        if (lazyInit) {
            LOG.debug("Creating lazy injection proxy for {}…", configuration.getRepositoryInterface());
        }

        return lazyInit;
    }
}