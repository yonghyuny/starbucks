package com.example.starbucks.controller;

import com.example.starbucks.dto.ApiResponse;
import com.example.starbucks.model.Coffee;
import com.example.starbucks.service.CoffeeService;
import com.example.starbucks.status.ResponseStatus;
import com.example.starbucks.status.ResultStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/coffee")
public class CoffeeController {

    @Autowired
    CoffeeService coffeeService;

    @GetMapping("all")
    public ResponseEntity<ApiResponse<List<Coffee>>> getAllCoffees() {
        List<Coffee> coffeeList = coffeeService.getAllCoffees();
        ApiResponse apiResponse = new ApiResponse(ResponseStatus.SUCCESS, "성공", coffeeList);
        return ResponseEntity.ok(apiResponse);
    }



    // <?>: java에서의 wildcard => (TS에서의 any와 같다고 생각하면 됨)
    public ApiResponse<?> validateApiResponse(ResultStatus status){
        ResponseStatus resultStatus =ResultStatus.FAIL.equals(status) ? ResponseStatus.FAIL : ResponseStatus.SUCCESS;
        String message = ResultStatus.FAIL.equals(status) ? "실패" : "성공";
        return new ApiResponse(resultStatus, message,null);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> addCoffee(@RequestBody Coffee coffee) {
        ResultStatus resultStatus = coffeeService.addCoffee(coffee);
        if(resultStatus.getResult().equals(ResultStatus.FAIL)) {
            return ResponseEntity.ok(new ApiResponse(ResponseStatus.FAIL, "실패", null));
        } else {
            return ResponseEntity.ok(new ApiResponse(ResponseStatus.SUCCESS, "성공", null));
        }
    }


    //api/v1/coffee/커피 아이디넘버
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<Optional<Coffee>>> getCoffeeByIs(@PathVariable Integer id) {
        coffeeService.getCoffeeById(id);
        Optional<Coffee> coffee = coffeeService.getCoffeeById(id);
        if (coffee.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(ResponseStatus.NOT_FOUND, "커피없음", null));
        } else {
            return ResponseEntity.ok(new ApiResponse<>(ResponseStatus.SUCCESS, "커피 있음", coffee));
        }
    }

    // 범위 조회
    @GetMapping("/price")
    public List<Coffee> getCoffeeByPrice(@RequestParam int min, @RequestParam int max) {
        return coffeeService.getCoffeesByPrice(min, max);
    }

    // 특정 메뉴 조회
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Coffee>>> getCoffeesByName(@RequestParam String name) {
        List<Coffee> coffeeList = coffeeService.getCoffeesByName(name);
        return ResponseEntity.ok(new ApiResponse<>(ResponseStatus.SUCCESS, "성공", coffeeList));
    }

}
