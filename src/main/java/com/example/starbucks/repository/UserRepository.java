package com.example.starbucks.repository;

import com.example.starbucks.model.UserCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // -> DB와 연결해주는 인터페이스
public interface UserRepository extends JpaRepository<UserCustom,Integer> {
    UserCustom findByUserId(String userId);
}
