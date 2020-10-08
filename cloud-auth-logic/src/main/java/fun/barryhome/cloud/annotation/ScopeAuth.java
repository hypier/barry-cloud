package fun.barryhome.cloud.annotation;

import java.lang.annotation.*;

/**
 * Created on 2020/10/8 11:09 上午
 *
 * @author barry
 * Description:
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
@Documented
public @interface ScopeAuth {

    String token() default "AUTH_TOKEN";
    String scope() default "";
    String[] scopes() default {};
}
