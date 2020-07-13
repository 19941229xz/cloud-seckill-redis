package com.example.order.service;

import com.example.order.pojo.Stock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    StockServiceImpl stockService;
    @Autowired
    RedisServiceImpl redisService;

    @Override
    public String doSeckill(String goodsId) {
        //生成锁的过期时间  和  redis锁的key值
        long expireTime = System.currentTimeMillis()+10000;
        String stockKey = "stock:seckill:"+goodsId;
        // 加锁 失败就返回没抢到
        if(!redisService.lock(stockKey,String.valueOf(expireTime))){
            return "手速慢了 没抢到。。。";
        }


        //查询库存
        Stock stock = stockService.getStockByGoodsId(goodsId);
        int stockNum = stock.getStockNum();
        //判断库存
        if(stockNum>=1){
            //减库存
            stock.setStockNum(stockNum-1);
            //模拟创建订单 结算订单的操作
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stockService.updateStock(stock);
            log.info("下单成功！剩余库存：{} 处理线程：{}",stock.getStockNum(),Thread.currentThread().getId());

            //解锁
            redisService.unlock(stockKey,String.valueOf(expireTime));
            return "下单成功";
        }else{
            //解锁
            redisService.unlock(stockKey,String.valueOf(expireTime));
            return "下单失败 库存不足";
        }

    }
}
