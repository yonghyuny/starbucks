package com.example.starbucks.repository;

import com.example.starbucks.model.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoffeeRepository extends JpaRepository<Coffee, Integer> {
    List<Coffee> findByName(String name);


    @Query(nativeQuery = true, value = "select * from Coffee c where c.price between min and max" )
//    @Query("select c from Coffee c where c.price between :min and :max")
    List<Coffee> findByPrice(@Param("min") int min , @Param("max")int max );


}
