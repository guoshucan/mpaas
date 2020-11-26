package ghost.framework.core.resolverFactory;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.resolver.ResolverCompatibleMode;
import ghost.framework.context.resolver.IResolverFactory;
import ghost.framework.context.resolver.IResolverFactoryContainer;
import ghost.framework.context.resolver.ResolverException;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * package: ghost.framework.core.resolver
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:解析器工厂容器类
 * @Date: 2020/3/18:17:39
 */
@Component
public class ResolverFactoryContainer
        extends AbstractCollection<IResolverFactory>
        implements IResolverFactoryContainer {
    public ResolverFactoryContainer(@Application @Autowired @Nullable IResolverFactoryContainer parent) {
        this.parent = parent;
    }

    private IResolverFactoryContainer parent;
    private List<IResolverFactory> list = new ArrayList<>();

    @Override
    public Iterator<IResolverFactory> iterator() {
        return list.iterator();
    }

    @Override
    public boolean add(IResolverFactory resolverFactory) {
        return list.add(resolverFactory);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public <S, T> void resolver(S source, T target) throws ResolverException{
        this.resolver(source, target, ResolverCompatibleMode.IgnoreNullAndEmpty);
    }

    @Override
    public <S, T> void resolver(S source, T target, ResolverCompatibleMode compatibleMode) throws ResolverException {
        for (IResolverFactory r : this.list) {
            if (r.isResolver(source.getClass(), target.getClass())) {
                r.resolver(source, target, compatibleMode);
                return;
            }
        }
        //
        if (this.parent != null) {
            this.parent.resolver(source, target, compatibleMode);
        }
    }
}