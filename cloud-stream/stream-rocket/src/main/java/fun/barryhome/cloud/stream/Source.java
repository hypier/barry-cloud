package fun.barryhome.cloud.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * Created on 2020/9/22 11:30 上午
 *
 * @author barry
 * Description: 取得 destination: minestream
 */
public interface Source {
    String OUTPUT = "myOutput";

    @Output(OUTPUT)
    MessageChannel message();


}
