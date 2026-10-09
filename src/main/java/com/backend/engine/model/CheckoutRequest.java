package com.backend.engine.model;

import lombok.Data;
import java.io.Serializable;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private Long productId;
    private int quantity;
}