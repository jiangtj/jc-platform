package com.jtj.cloud.feignclient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by MrTT (jiang.taojie@foxmail.com)
 * 2018/7/31.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class User {

    private String name;
    private Integer age;
    private Integer sex;

}
