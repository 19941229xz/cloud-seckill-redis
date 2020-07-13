package com.example.order.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean lock(String key,String value) {

        //加锁
        if(redisTemplate.opsForValue().setIfAbsent(key,value)){
            return true;
        }
        //如果加锁失败  说明以及有锁了 获取当前的锁的内容
        String currentValue = redisTemplate.opsForValue().get(key);
        // (解锁可能会出现失败的情况) 所以要判断锁是否过期 防止出现死锁
        if(!StringUtils.isEmpty(currentValue)&&
        Long.parseLong(currentValue) < System.currentTimeMillis()){
            //获取上一步锁的时间 并设置新的时间
            String oldValue = redisTemplate.opsForValue().getAndSet(key,value);
            if(!StringUtils.isEmpty(oldValue)&&oldValue.equals(currentValue)){
                return true;
            }
        }

        return false;
    }

    @Override
    public void unlock(String key,String value) {
        try{
            String currentValue = redisTemplate.opsForValue().get(key);
            //验证锁
            if(StringUtils.isEmpty(currentValue)&&currentValue.equals(value)){
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.info("redis解锁失败");
        }
    }
}
