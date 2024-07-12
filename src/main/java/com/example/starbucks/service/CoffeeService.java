package com.example.starbucks.service;

import com.example.starbucks.model.Coffee;
import com.example.starbucks.status.ResultStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;


public interface CoffeeService {
    List<Coffee> getAllCoffees();
    List<Coffee> getCoffeesByPrice(int min,int max);
    List<Coffee> getCoffeesByName(String name);

    ResultStatus addCoffee(Coffee coffee);
    Optional<Coffee> getCoffeeById(Integer id);
}
