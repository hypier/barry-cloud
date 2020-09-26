package fun.barryhome.cloud.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
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

    @StreamListener(value = Sink.MY_INPUT_1, condition = "headers['version']=='1.0'")
    public void receiveSucceed_v1(@Payload String message) {
        String msg = "StreamReceiver v1: " + message;
        log.error(msg);
    }

    @StreamListener(value = Sink.MY_INPUT_1, condition = "headers['version']=='2.0'")
    public void receiveSucceed_v2(String message) {
        log.error("StreamReceiver v2: {}", message);
    }

    @StreamListener(Sink.MY_INPUT_2)
    public void receiveFailed(String message) {
        throw new RuntimeException("error");
    }


    @StreamListener(value = Sink.INPUT_STEP_1)
    @SendTo(Source.OUTPUT_STEP_2)
    public String receiveStep_1(String message) {
        String msg = "receiveAndSend-step1:" + message;
        log.error(msg);
        return msg;
    }

    @StreamListener(Sink.INPUT_STEP_2)
    public void receiveStep_2(String message) {
        log.error("StreamReceiver step2: {}", message);
    }
}