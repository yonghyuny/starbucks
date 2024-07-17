package com.example.starbucks.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@RequiredArgsConstructor
@Table(name="user")
public class UserCustom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @Column(name ="user_id")
    private String userId;

    @NonNull
    @Column(name ="pwd")
    private String pwd;

    @NonNull
    @Column(name ="nickname")
    private String nickname;



}
