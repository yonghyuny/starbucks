package com.example.starbucks.dto;

import com.example.starbucks.status.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse <T> {
    private ResponseStatus status;
    private String message;
    private T data;



}
