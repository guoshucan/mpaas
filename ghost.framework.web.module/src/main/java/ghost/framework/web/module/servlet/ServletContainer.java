package ghost.framework.web.module.servlet;

import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.web.context.servlet.context.IServletContainer;

import javax.servlet.Servlet;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
@Component
public class ServletContainer extends AbstractMap<String, Servlet> implements IServletContainer {


    private Map<String, Servlet> map = new HashMap<>();

    @Override
    public Set<Entry<String, Servlet>> entrySet() {
        return map.entrySet();
    }

    @Override
    public Servlet put(String key, Servlet value) {
        return map.put(key, value);
    }
}
