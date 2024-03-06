package com.jiangtj.platform.baseservlet;

import com.jiangtj.platform.common.validation.MobilePhone;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("food")
public class FoodController {

    @PostMapping("create")
    public Food createFood(@Valid @RequestBody Food food){
        return food;
    }

    @PostMapping("pay1")
    public String payFood1(@MobilePhone String phone, @NotBlank String name){
        return phone + " paid " + name;
    }

    @PostMapping("pay2")
    public String payFood2(@MobilePhone Long phone, @NotBlank String name){
        return phone + " paid " + name;
    }

    public record Food(@NotBlank String name, @Min(0) int price) {
    }
}
