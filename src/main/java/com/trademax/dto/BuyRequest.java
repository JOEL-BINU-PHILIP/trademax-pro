package com.trademax.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyRequest {
    private String userId;
    private String ticker;
    private int quantity;
}
