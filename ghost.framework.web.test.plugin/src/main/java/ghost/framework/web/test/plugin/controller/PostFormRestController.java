package ghost.framework.web.test.plugin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.web.context.bind.annotation.RequestBody;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.bind.annotation.RestController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Objects;

/**
 * package: ghost.framework.web.test.plugin.controller
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:表达提交测试
 * @Date: 2020/2/3:22:51
 */
@RestController("/postform")
public class PostFormRestController {
     private Log log = LogFactory.getLog(PostFormRestController.class);
    @Application
    @Autowired
    private ObjectMapper mapper;
    @RequestMapping(value = "/form")
    public Object form(@RequestBody Body body, HttpServletRequest request) throws JsonProcessingException {
        System.out.println("form:" + this.mapper.writeValueAsString(body));
        return body;
    }
    @RequestMapping(value = "/formValid")
    public Object formValid(@RequestBody @Valid Body body, HttpServletRequest request) throws JsonProcessingException {
        System.out.println("form:" + this.mapper.writeValueAsString(body));
        return body;
    }
    public class Body {
        private boolean n;

        public boolean isN() {
            return n;
        }

        public void setN(boolean n) {
            this.n = n;
        }

        private int i;

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Body{" +
                    "n=" + n +
                    ", i=" + i +
                    ", name='" + name + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Body body = (Body) o;
            return n == body.n &&
                    i == body.i &&
                    name.equals(body.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(n, i, name);
        }
    }
}