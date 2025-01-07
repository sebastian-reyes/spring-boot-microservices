package com.demo.items.configuration.clients;

import com.demo.items.models.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "products", path = "/api/products")
public interface ProductFeignClient {

    @GetMapping
    List<ProductDto> findAll();

    @GetMapping("/{id}")
    ProductDto getProductById(@PathVariable Long id);
}
