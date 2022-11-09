package com.rio.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;

    //因为业务类有可能返回一个 null 所以要提供一个不含有 data 的构造方法供业务层使用
    public CommonResult(Integer code, String message) {
        this(code, message, null);
    }
}
