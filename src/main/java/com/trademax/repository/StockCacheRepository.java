package com.trademax.repository;

import com.trademax.model.StockCache;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockCacheRepository extends MongoRepository<StockCache, String> {
}
