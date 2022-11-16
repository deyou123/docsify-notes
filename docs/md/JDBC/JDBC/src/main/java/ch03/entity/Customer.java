package ch03.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;
import java.util.Date;

/**
 * @author DeYou
 * @date 2022/8/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private int id;
    private String name;
    private String email;
    private Date birth;
    private Blob photo;

    public Customer(Integer id, String name, String email, java.sql.Date birth) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birth = birth;
    }
}