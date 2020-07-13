package com.example.order.service;

import com.example.order.pojo.Stock;

public interface StockService {


    public Stock getStockByGoodsId(String goodsId);

    public Stock updateStock(Stock stock);


}
