package fun.barryhome.cloud.annotation;


import fun.barryhome.cloud.AuthQuerySupplier;
import fun.barryhome.cloud.ScopeAuthAdapter;
import fun.barryhome.cloud.util.AccessDeniedException;
import fun.barryhome.cloud.util.ParseSPEL;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created on 2020/10/8 11:09 上午
 *
 * @author barry
 * Description:
 */
@Aspect
@Component
public class ScopeAuthAdvice {

    @Autowired
    private AuthQuerySupplier supplier;

    private final String SPEL_FLAG = "#";

    @Around("@annotation(scopeAuth)")
    public Object before(ProceedingJoinPoint thisJoinPoint, ScopeAuth scopeAuth) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) thisJoinPoint.getSignature();
        // 获取方法参数
        Object[] args = thisJoinPoint.getArgs();

        // 获取token
        String authToken = getToken(args, scopeAuth.token(), methodSignature.getMethod());
        if (StringUtils.isEmpty(authToken)) {
            throw new AccessDeniedException("110", "没有找到AUTH_TOKEN");
        }


        if (StringUtils.isEmpty(scopeAuth.scopes()) || scopeAuth.scopes().length <= 0) {
            setScope(scopeAuth.scope(), methodSignature, args, authToken);
        } else {
            for (String scope : scopeAuth.scopes()) {
                setScope(scope, methodSignature, args, authToken);
            }
        }


        return thisJoinPoint.proceed();
    }

    /**
     * 设置范围
     *
     * @param scope
     * @param methodSignature
     * @param args
     * @param authToken
     */
    private void setScope(String scope, MethodSignature methodSignature, Object[] args, String authToken) {
        // 获取请求范围
        Set<String> requestScope = getRequestScope(args, scope, methodSignature.getMethod());

        ScopeAuthAdapter adapter = new ScopeAuthAdapter(supplier);
        // 已授权范围
        Set<String> authorizedScope = adapter.identifyPermissionScope(authToken, requestScope);

        if (authorizedScope == null || authorizedScope.size() <= 0) {
            throw new AccessDeniedException("101", "没有可访问的权限范围");
        }

        // 设置新范围
        setRequestScope(args, scope, authorizedScope, methodSignature.getMethod());
    }

    /**
     * 获取 token
     *
     * @param args
     * @param tokenKey
     * @param method
     * @return
     */
    private String getToken(Object[] args, String tokenKey, Method method) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        tokenKey = StringUtils.isEmpty(tokenKey) ? "AUTH_TOKEN" : tokenKey;

        String token = request.getHeader(tokenKey);
        if (StringUtils.isEmpty(token) && request.getAttribute(tokenKey) != null) {
            token = request.getAttribute(tokenKey).toString();
        }

        // 解析 SPEL 表达式
        if (StringUtils.isEmpty(token) && tokenKey.indexOf(SPEL_FLAG) == 0) {
            token = ParseSPEL.parseMethodKey(tokenKey, method, args, String.class);
        }

        if (StringUtils.isEmpty(token)){
            token = tokenKey;
        }

        return token;
    }

    /**
     * 设置请的请求范围
     *
     * @param args
     * @param scopeName
     * @param method
     */
    private void setRequestScope(Object[] args, String scopeName, Collection<String> scopeValues, Method method) {

        // 解析 SPEL 表达式
        if (scopeName.indexOf(SPEL_FLAG) == 0) {
            ParseSPEL.setMethodValue(scopeName, scopeValues, method, args);
        }
    }

    /**
     * 获取请求权限范围
     *
     * @param args
     * @param scopeName
     * @param method
     * @return
     */
    private Set<String> getRequestScope(Object[] args, String scopeName, Method method) {

        Collection<String> scopeList;

        if (StringUtils.isEmpty(scopeName)) {
            return null;
        }

        // 解析 SPEL 表达式
        if (scopeName.indexOf(SPEL_FLAG) == 0) {
            scopeList = ParseSPEL.parseMethodKey(scopeName, method, args, Collection.class);
            if (scopeList == null) {
                return null;
            }
        } else {
            return null;
        }

        Set<String> list = new HashSet<String>();
        list.addAll(scopeList);

        return list;
    }


}
