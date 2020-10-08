package fun.barryhome.cloud.util;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Created on 2020/10/8 11:09 上午
 *
 * @author barry
 * Description: SPEL表达式解析
 */
public class ParseSPEL {

    /**
     * 解析方式上的表达式
     * key 定义在注解上，支持SPEL表达式
     *
     * @param key
     * @param method
     * @param args
     * @return
     */
    public static <T> T parseMethodKey(String key, Method method, Object[] args, Class<T> t) {

        // 获取被拦截方法参数名列表(使用Spring支持类库)
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = u.getParameterNames(method);

        // 使用SPEL进行key的解析
        ExpressionParser parser = new SpelExpressionParser();
        // SPEL 上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        // 把方法参数放入SPEL上下文中
        for (int i = 0; i < paraNameArr.length; i++) {
            context.setVariable(paraNameArr[i], args[i]);
        }

        return parser.parseExpression(key).getValue(context, t);

    }

    /**
     * 解析方式上的表达式
     * key 定义在注解上，支持SPEL表达式
     *
     * @param key
     * @param method
     * @param args
     * @return
     */
    public static void setMethodValue(String key, Collection<String> values, Method method, Object[] args) {

        // 获取被拦截方法参数名列表(使用Spring支持类库)
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = u.getParameterNames(method);

        // 使用SPEL进行key的解析
        ExpressionParser parser = new SpelExpressionParser();
        // SPEL 上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        // 把方法参数放入SPEL上下文中
        for (int i = 0; paraNameArr != null && i < paraNameArr.length; i++) {
            context.setVariable(paraNameArr[i], args[i]);
            parser.parseExpression(key).setValue(context, values);
        }


    }
}
