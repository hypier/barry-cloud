package fun.barryhome.cloud.annotation;

import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created on 2020/10/8 11:09 上午
 *
 * @author barry
 * Description:
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
@Documented
@Import(AutoConfigurationImportSelector.class)
public @interface EnableScopeAuth {
}
