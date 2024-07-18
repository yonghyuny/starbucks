package com.example.starbucks.service.coffeeService;

import com.example.starbucks.model.Coffee;
import com.example.starbucks.repository.CoffeeRepository;
import com.example.starbucks.status.ResultStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoffeeServiceImpl implements CoffeeService {


    @Autowired
    private CoffeeRepository coffeeRepository;


    // 모든 커피 검색
    @Override
    public List<Coffee> getAllCoffees() {
        return coffeeRepository.findAll();
    }

    // 커피 가격대설정후 검색
    @Override
    public List<Coffee> getCoffeesByPrice(int min, int max) {
        return coffeeRepository.findByPrice(min,max);
    }

    // 커피 이름 검색
    @Override
    public List<Coffee> getCoffeesByName(String name) {
        return coffeeRepository.findByName(name);
    }

    // 커피 등록
    @Override
    public ResultStatus addCoffee(Coffee coffee) {
        if(coffee.getName() == null || coffee.getName().isEmpty()){
            return ResultStatus.FAIL;
        }
        if(coffee.getPrice() < 0){
            return ResultStatus.FAIL;
        }
        coffeeRepository.save(coffee);
        return ResultStatus.SUCCESS;
    }

    @Override
    public Optional<Coffee> getCoffeeById(Integer id) {
       return  coffeeRepository.findById(id);

    }
}
