package com.jgcosmetics.store.dto;

import lombok.Data;

@Data
public class AddCartRequest {
    private Long userId;
    private Long productId;
    private Integer quantity;
    private String sessionId;
    private String shade;
}
