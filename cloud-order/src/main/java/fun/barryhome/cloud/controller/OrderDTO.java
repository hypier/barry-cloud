package fun.barryhome.cloud.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created on 2020/10/8 4:48 下午
 *
 * @author barry
 * Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    /**
     * 订单号
     */
    private String orderID;
    /**
     * 城市
     */
    private String[] cities;
}
