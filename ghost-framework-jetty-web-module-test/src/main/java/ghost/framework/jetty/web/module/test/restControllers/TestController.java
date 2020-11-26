package ghost.framework.jetty.web.module.test.restControllers;
import ghost.framework.web.context.bens.annotation.RequestMapping;
import ghost.framework.web.context.bens.annotation.RequestMethod;
import ghost.framework.web.context.bens.annotation.RestController;

import java.util.UUID;

@RestController(value = "/test")
public class TestController {
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Object get(){
        return UUID.randomUUID().toString();
    }
    @RequestMapping(value = "/get1", method = RequestMethod.GET)
    public Object get1(){
        return UUID.randomUUID().toString();
    }
}