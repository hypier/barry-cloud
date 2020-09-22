package fun.barryhome.cloud.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

/**
 * Created on 2020/9/22 11:32 上午
 *
 * @author barry
 * Description:
 */
@Slf4j
@Component
public class StreamReceiver {

    @StreamListener(Receiver.MY_RECEIVER_1)
    public void receive1(String message) {
        log.error("StreamReceiver: {}", message);
    }

    @StreamListener(Receiver.MY_RECEIVER_2)
    public void receive2(String message) {
        log.error("StreamReceiver1: {}", message);
    }
}