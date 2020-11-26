package ghost.framework.web.test.plugin.controller;

import ghost.framework.beans.BeanException;
import ghost.framework.context.converter.ConverterException;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.context.resolver.ReturnValueResolverException;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.bind.annotation.RestController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * package: ghost.framework.web.test.plugin.controller
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/28:21:21
 */
@RestController("/exception")
public class ExceptionRestController {
     private Log log = LogFactory.getLog(ExceptionRestController.class);

    @RequestMapping("exception")
    public Object exception() throws Exception {
        throw new Exception("exception");
    }

    @RequestMapping("beanException")
    public Object beanException() throws BeanException {
        throw new BeanException("BeanException");
    }

    @RequestMapping("returnValueResolverException")
    public Object returnValueResolverException() throws ReturnValueResolverException {
        throw new ReturnValueResolverException("ReturnValueResolverException");
    }

    @RequestMapping("resolverException")
    public Object resolverException() throws BeanException {
        throw new ResolverException("ResolverException");
    }

    @RequestMapping("converterException")
    public Object ConverterException() throws ConverterException {
        throw new ConverterException("converterException");
    }
}
