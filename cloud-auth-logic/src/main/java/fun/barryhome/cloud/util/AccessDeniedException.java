package fun.barryhome.cloud.util;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

/**
 * Created by heyong on 2017/11/24 17:08
 * Description: 禁止访问异常
 * @author heyong
 */
@Data
@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessDeniedException extends RuntimeException {

    /**
     * 错误代码
     */
    private String errorCode;
    /**
     * 错误说明,此说明只显示在后端、日志中，不显示在前端，此处由 GlobalExceptionHandler 处理
     */
    private String errorMessage;
    /**
     * 抛出对象,在日志中记录
     */
    private Object[] payload;

    public AccessDeniedException(String errorCode, String errorMessage) {
        super(MessageFormat.format("*[{0}] {1}", errorCode, errorMessage));
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}
