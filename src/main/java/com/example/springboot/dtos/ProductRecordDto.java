package com.example.springboot.dtos;

public record ProductRecordDto(@NotBlank String name, @NotNull BigDecimal value) {
    

}