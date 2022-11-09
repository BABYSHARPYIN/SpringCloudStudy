package com.rio.service;

import com.rio.entities.CommonResult;
import com.rio.entities.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author lixiaolong
 * @date 2020-02-19 23:59
 */
@Component
@FeignClient(value = "CLOUD-PAYMENT-SERVICE") //设置服务名称
public interface PaymentFeignService {
    @GetMapping(value = "/payment/get/{id}")//查找服务地址
    CommonResult<Payment> getPaymentById(@PathVariable("id") Long id);

    @GetMapping(value = "/payment/feign/timeout")
    String paymentFeignTimeout();
}
