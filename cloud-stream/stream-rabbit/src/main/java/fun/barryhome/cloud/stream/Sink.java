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
    String MY_INPUT_1 = "myInput-1";
    String MY_INPUT_2 = "myInput-2";

    String INPUT_STEP_1 = "input-step-1";
    String INPUT_STEP_2 = "input-step-2";

    @Input(MY_INPUT_1)
    SubscribableChannel sub1();

    @Input(MY_INPUT_2)
    SubscribableChannel sub2();

    @Input(INPUT_STEP_1)
    SubscribableChannel sub3();

    @Input(INPUT_STEP_2)
    SubscribableChannel sub4();
}
