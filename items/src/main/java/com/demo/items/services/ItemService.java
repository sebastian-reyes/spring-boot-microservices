package com.demo.items.services;

import com.demo.items.models.Item;
import com.demo.items.models.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<Item> findAll();
    Optional<Item> findById(Long id);
    ProductDto save(ProductDto productDto);
    ProductDto update(Long id, ProductDto productDto);
    void deleteById(Long id);
}
