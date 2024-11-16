package com.demo.items.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private ProductDto product;
    private int quantity;

    public Double getTotalPrice() {
        return product.getPrice() * quantity;
    }
}
