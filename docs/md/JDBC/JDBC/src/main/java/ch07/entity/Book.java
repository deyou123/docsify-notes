package ch07.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DeYou
 * @date 2022/8/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Book {
    private Integer id;
    /**
     * 书名
     */
    private String title;
    private String author; // 作者
    private double price; // 价格
    private Integer sales; // 销量
    private Integer stock; // 库存
    private String imgPath = "static/img/default.png"; // 封面图片的路径
}