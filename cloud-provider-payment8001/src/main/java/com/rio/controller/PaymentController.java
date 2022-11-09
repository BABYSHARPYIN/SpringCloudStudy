package com.rio.controller;

import com.rio.entities.CommonResult;
import com.rio.entities.Payment;
import com.rio.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class PaymentController {
    @Autowired
    PaymentService paymentService;

    @Value("${server.port}")
    String serverPort;

    @Autowired
    private DiscoveryClient discoveryClient;

    @PostMapping(value = "/payment/create")
    public CommonResult create( @RequestBody Payment payment){
        System.out.println(payment);
        int i = paymentService.create(payment);
        log.info("======>插入结果:"+i);
        return i>0?new CommonResult(200, "插入成功"):new CommonResult(500, "插入失败");
    }

    @GetMapping(value = "/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id) {
        Payment paymentById = paymentService.getPaymentById(id);
        log.info("======>查询结果:"+paymentById);
        return paymentById!=null?new CommonResult(200, String.format("查询成功,端口号: %s",serverPort),paymentById):new CommonResult(500, "查询失败");
    }

    @GetMapping("/payment/discovery")
    public Object discovery() {
        List<String> services = discoveryClient.getServices();
        services.forEach(log::info);
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        instances.forEach(instance->log.info(String.format("%s \t %s \t %s \t %s",instance.getServiceId(),instance.getHost(),instance.getUri(),instance.getPort())));
        return discoveryClient;
    }

    @GetMapping(value="/payment/feign/timeout")
    public String paymentFeignTimeout(){
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return serverPort;
    }


}
