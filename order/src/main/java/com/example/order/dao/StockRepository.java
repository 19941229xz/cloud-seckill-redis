package com.example.order.dao;

import com.example.order.pojo.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock,String> {

    Stock getStockByGoodsId(String goodsId);


}
