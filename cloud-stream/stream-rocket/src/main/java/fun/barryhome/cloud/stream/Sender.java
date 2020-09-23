package fun.barryhome.cloud.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * Created on 2020/9/22 11:30 上午
 *
 * @author barry
 * Description: 取得 destination: minestream
 */
public interface Sender {
    String OUTPUT = "mySender";
    String OUTPUT_STEP_2 = "sender-step-2";

    @Output(OUTPUT)
    MessageChannel message();

    @Output(OUTPUT_STEP_2)
    MessageChannel message2();

}
