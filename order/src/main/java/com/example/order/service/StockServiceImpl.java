package com.example.order.service;

import com.example.order.dao.StockRepository;
import com.example.order.pojo.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class StockServiceImpl implements StockService {



    @Autowired
    StockRepository stockRepository;

    @Cacheable(value = "stocks",key = "#p0")
    @Override
    public Stock getStockByGoodsId(String goodsId) {

        return stockRepository.getStockByGoodsId(goodsId);
    }

    @CacheEvict(value = "stocks",key = "#p0.goodsId")
    @Override
    public Stock updateStock(Stock stock) {
        return stockRepository.save(stock);
    }
}
