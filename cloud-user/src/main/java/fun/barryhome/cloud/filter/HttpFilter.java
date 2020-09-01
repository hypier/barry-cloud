package fun.barryhome.cloud.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on 2020/8/16 10:33 上午
 *
 * @author barry
 * Description: 此类用于拦截请求地址，在功能上没有用途
 */
@Slf4j
public class HttpFilter extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("request请求地址path[{}] uri[{}]", request.getServletPath(),request.getRequestURI());
        return super.preHandle(request, response, handler);
    }
}

@Configuration
class WebAppConfigurer extends WebMvcConfigurationSupport {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HttpFilter()).addPathPatterns("/**");
    }
}