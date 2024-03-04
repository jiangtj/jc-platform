package com.jiangtj.platform.baseservlet;

import jakarta.validation.Valid;
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
}
