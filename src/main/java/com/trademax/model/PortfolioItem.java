package com.trademax.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioItem {
    private String ticker;
    private int quantity;
    private double avgBuyPrice;
}
