package ch03.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author DeYou
 * @date 2022/8/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private int orderId;
    private String orderName;
    private Date orderDate;
}