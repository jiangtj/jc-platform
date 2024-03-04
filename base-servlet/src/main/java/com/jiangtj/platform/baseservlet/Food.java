package com.jiangtj.platform.baseservlet;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Food {
    @NotBlank
    private String name;
    @Min(0)
    private int price;
}
