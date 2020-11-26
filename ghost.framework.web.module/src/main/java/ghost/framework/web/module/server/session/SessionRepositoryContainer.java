package ghost.framework.web.module.server.session;

import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.web.session.core.ISessionRepositoryContainer;
import ghost.framework.web.session.core.Session;
import ghost.framework.web.session.core.SessionRepository;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * package: ghost.framework.web.session.core
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:会话仓库容器
 * 存储继承 {@link SessionRepository} 的会话存储对象容器
 * @Date: 2020/2/27:17:14
 */
@Component
public class SessionRepositoryContainer<S extends Session, R extends SessionRepository<S>> extends AbstractCollection<R> implements ISessionRepositoryContainer<S, R> {
    /**
     * 获取默认存储仓库
     * 按照存储列表第一个位置获取，并设置第一个位置为默认会话操作仓库
     *
     * @return
     */
    @Override
    public SessionRepository<S> getDefault() {
        for (SessionRepository repository : this.list) {
            if (repository.isDefault()) {
                return repository;
            }
        }
        SessionRepository<S> repository = list.get(0);
        repository.setDefault(true);
        return repository;
    }

    /**
     * 会话存储操作仓库列表
     */
    private List<R> list = new ArrayList<>();

    @Override
    public boolean add(R r) {
        synchronized (list) {
            //如果新增为默认仓库，侧设置其它不为默认仓库
            if (r.isDefault()) {
                list.forEach(a -> a.setDefault(false));
            }
            return list.add(r);
        }
    }

    @Override
    public Iterator<R> iterator() {
        return list.iterator();
    }

    @Override
    public int size() {
        return list.size();
    }
}