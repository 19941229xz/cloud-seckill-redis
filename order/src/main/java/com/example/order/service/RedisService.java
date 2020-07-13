
package com.example.order.service;

public interface RedisService {


    public boolean lock(String key,String value);

    public void unlock(String key,String value);


}
