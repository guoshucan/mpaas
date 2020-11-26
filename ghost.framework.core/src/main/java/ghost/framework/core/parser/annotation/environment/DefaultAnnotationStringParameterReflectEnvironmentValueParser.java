package ghost.framework.core.parser.annotation.environment;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.module.Module;
import ghost.framework.context.environment.IEnvironment;
import ghost.framework.context.exception.AnnotationReflectEnvironmentException;
import ghost.framework.util.AnnotationReflectUtil;
import ghost.framework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Map;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:默认注释替换env值解析器类
 * @Date: 20:05 2020/1/5
 */
public class DefaultAnnotationStringParameterReflectEnvironmentValueParser implements AnnotationStringParameterReflectEnvironmentValueParser {
    /**
     * 注入应用或模块env接口
     */
    private IEnvironment environment;
    /**
     * 初始化默认注释替换env值解析器类
     * 应用时注入应用env
     * 模块时注入模块env
     * @param environment
     */
    public DefaultAnnotationStringParameterReflectEnvironmentValueParser(@Application @Module @Autowired IEnvironment environment){
        this.environment = environment;
    }
    /**
     * 解析替换注释env值
     * @param a
     */
    @Override
    public void annotationReflectValue(Annotation a) {
        try {
            //反射代理
            InvocationHandler ih = Proxy.getInvocationHandler(a);
            //获取注释的map参数属性
            Field f = ih.getClass().getDeclaredField(AnnotationReflectUtil.MEMBER_VALUES);
            //设置权限
            f.setAccessible(true);
            //获取属性的map对象注释参数
            Map<String, Object> m = (Map) f.get(ih);
            //遍历注释参数是否带${xxx.xx}的格式在env中搜索指定的参数值填充
            for (String key : new ArrayList<>(m.keySet())) {
                Object value = m.get(key);
                //验证注释参数类型是否为字符串类型跟需要替换配置参数
                if (value instanceof String) {
                    String str = value.toString();
                    if (StringUtils.isEmpty(str)) {
                        continue;
                    }
//                    try {
//                        m.put(key, this.environment.replaceMiddle(str));
//                        System.out.println(m.get(key).toString());
//                    } catch (EnvironmentInvalidException e) {
//                        //判断错误是否引发错误，主要看注释是否带错误参数
//                        if (m.containsKey(AnnotationReflectUtil.ERROR) &&
//                                m.get(AnnotationReflectUtil.ERROR) instanceof Boolean &&
//                                (boolean) m.get(AnnotationReflectUtil.ERROR)) {
//                            throw e;
//                        } else {
//                            throw e;
//                        }
//                    }
//                if (StringUtil.isMiddle(str, "${", "}")) {
//                    m.put(value,
//                            //替换${xxx.xx}中间内容
//                            StringUtil.replaceMiddle(str, "${", "}",
//                                    //获取env的${xxx.xx}中间的配置内容
//                                    env.get(StringUtil.getMiddle(str, "${", "}"))));
//                }
                }
            }
            //遍历下级注释
            for (Map.Entry<String, Object> entry : m.entrySet()) {
                //判断注释参数是否为注释
                if (Annotation.class.isAssignableFrom(entry.getValue().getClass())) {
                    //设置注释参数替换，同时继续向注释下级替换
                    this.annotationReflectValue((Annotation) entry.getValue());
                    continue;
                }
                //判断注释参数是否为数组对象
                if (entry.getValue().getClass().isArray()) {
                    //获取数组注释参数
                    Object[] objects = (Object[]) entry.getValue();
                    //遍历数组注释参数
                    for (Object o : objects) {
                        //判断注释参数是否为注释
                        if (Annotation.class.isAssignableFrom(o.getClass())) {
                            //设置注释参数替换，同时继续向注释下级替换
                            this.annotationReflectValue((Annotation) o);
                            continue;
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new AnnotationReflectEnvironmentException(e);
        }
    }
}