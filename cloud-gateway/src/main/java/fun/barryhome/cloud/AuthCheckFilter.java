package fun.barryhome.cloud;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created on 2020/8/15 9:35 下午
 *
 * @author barry
 * Description:
 */

@Slf4j
@Component
public class AuthCheckFilter extends AbstractGatewayFilterFactory {

    @Autowired
    private Session session;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            String token = request.getHeaders().getFirst("token");

            log.info("AppAuthCheckFilter当前请求的url:{}, method:{}", request.getURI().getPath(), request.getMethodValue());

            if (Strings.isEmpty(token)) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            // 是否是已登陆用户
            LoginUser loginUser = this.session.getSession(token);
            if (loginUser == null) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            // 将用户名传递给后端服务
            ServerWebExchange build;
            try {
                ServerHttpRequest host = exchange.getRequest().mutate()
                        .header("X-User-Name", loginUser.userName)
                        // 中文字符需要编码
                        .header("X-Real-Name", URLEncoder.encode(loginUser.realName, "utf-8"))
                        .build();
                build = exchange.mutate().request(host).build();
            } catch (UnsupportedEncodingException e) {
                build = exchange;
            }

            return chain.filter(build);
        };

    }
}