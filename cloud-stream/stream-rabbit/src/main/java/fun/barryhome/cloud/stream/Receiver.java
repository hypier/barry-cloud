package fun.barryhome.cloud.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * Created on 2020/9/22 5:26 下午
 *
 * @author barry
 * Description:
 */
public interface Receiver {
    String MY_RECEIVER_1 = "myReceiver-1";
    String MY_RECEIVER_2 = "myReceiver-2";

    @Input(MY_RECEIVER_1)
    SubscribableChannel sub1();

    @Input(MY_RECEIVER_2)
    SubscribableChannel sub2();
}
