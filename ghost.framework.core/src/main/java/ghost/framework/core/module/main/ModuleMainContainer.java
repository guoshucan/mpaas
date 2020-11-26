package ghost.framework.core.module.main;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Service;
import ghost.framework.beans.annotation.module.Module;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.module.IModule;
import ghost.framework.context.module.main.IModuleMainContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块运行主类容器
 * @Date: 12:03 2019/12/19
 */
@Service
public class ModuleMainContainer implements IModuleMainContainer {
    @Autowired
    private IApplication app;
    @Module
    @Autowired
    private IModule module;
    private List<Object> list = new ArrayList<>();

    @Override
    public void remove(Object main) {
        synchronized (list) {
            this.list.remove(main);
        }
    }

    @Override
    public boolean contains(Object main) {
        synchronized (list) {
            return this.list.contains(main);
        }
    }

    @Override
    public void add(Object main) {
        synchronized (list) {
            this.list.add(main);
        }
    }
}