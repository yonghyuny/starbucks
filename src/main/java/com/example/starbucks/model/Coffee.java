package com.example.starbucks.model;

import jakarta.persistence.*;
import lombok.*;


@Data
@NoArgsConstructor
// new Coffee()
@AllArgsConstructor
// new Coffee(id,name,price)
@RequiredArgsConstructor
// new Coffee(name,price) by NonNull
@Entity
@Table
public class Coffee {

    //db key 설정 및 자동증가
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NonNull
    private String name;
    @NonNull
    private int price;


}
