package com.trademax.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellRequest {
    private String userId;
    private String ticker;
    private int quantity;
}
