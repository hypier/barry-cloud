package fun.barryhome.cloud.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
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

    @StreamListener(value = Sink.INPUT)
    public void receiveSucceed_v1(@Payload String message) {
        String msg = "StreamReceiver1 v1: " + message;
        log.error(msg);
    }

}