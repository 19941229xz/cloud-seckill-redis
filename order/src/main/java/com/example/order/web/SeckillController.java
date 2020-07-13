package com.example.order.web;

import com.example.order.service.SeckillServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeckillController {

    @Autowired
    SeckillServiceImpl seckillService;

    @GetMapping("/doSeckill/{goodsId}")
    public String doSeckill(@PathVariable("goodsId") String goodsId){
        return seckillService.doSeckill(goodsId);
    }


}
