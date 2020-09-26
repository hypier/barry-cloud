package fun.barryhome.cloud.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * Created on 2020/9/22 5:26 下午
 *
 * @author barry
 * Description:
 */
public interface Sink {
    String INPUT = "myInput";

    @Input(INPUT)
    SubscribableChannel sub1();
}
