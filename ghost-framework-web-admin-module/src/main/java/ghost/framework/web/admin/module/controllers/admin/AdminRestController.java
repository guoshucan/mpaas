package ghost.framework.web.admin.module.controllers.admin;
import ghost.framework.web.context.bens.annotation.RequestBody;
import ghost.framework.web.context.bens.annotation.RequestMapping;
import ghost.framework.web.context.bens.annotation.RequestMethod;
import ghost.framework.web.context.bens.annotation.RestController;
import ghost.framework.web.module.responseContent.Response;
import ghost.framework.web.admin.module.entitys.AdminRoleEntity;
@RestController(path = "/api")
public class AdminRestController {
    @RequestMapping(path = "/2bb2fb27-e1e1-4a8d-a749-c184eda72982", method = RequestMethod.POST)
    public Response Add(@RequestBody AdminRoleEntity request) {
        Response r = new Response();
        try {

        } catch (Exception e) {

        }
        return null;
    }
}