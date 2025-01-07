package com.demo.items.services.impl;

import com.demo.items.configuration.clients.ProductFeignClient;
import com.demo.items.models.Item;
import com.demo.items.models.ProductDto;
import com.demo.items.services.ItemService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceFeignClientImpl implements ItemService {

    private final ProductFeignClient productFeignClient;

    @Override
    public List<Item> findAll() {
        return productFeignClient.findAll()
                .stream()
                .map(product -> new Item(product, new Random().nextInt(10) + 1))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> findById(Long id) {
        try {
            ProductDto product = productFeignClient.getProductById(id);
            return Optional.of(new Item(product, new Random().nextInt(10) + 1));
        } catch (FeignException e) {
            return Optional.empty();
        }
    }
}