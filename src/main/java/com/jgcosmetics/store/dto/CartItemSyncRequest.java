package com.jgcosmetics.store.dto;

import lombok.Data;

@Data
public class CartItemSyncRequest {
    private Long productId;
    private Integer quantity;
    private String sessionId;
    private Long userId;
    private String shade;
}
