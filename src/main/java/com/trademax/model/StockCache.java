package com.trademax.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "stocks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockCache {
    @Id
    private String ticker;
    private String company;
    private double price;
    private Instant fetchedAt;
}

