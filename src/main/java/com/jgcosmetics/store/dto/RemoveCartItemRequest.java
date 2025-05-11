package com.jgcosmetics.store.dto;

import lombok.Data;

@Data
public class RemoveCartItemRequest {
    private Long userId;
    private String sessionId;
    private Long productId;
    private String shade;
}
