package com.demo.items.controllers;

import com.demo.items.models.Item;
import com.demo.items.models.ProductDto;
import com.demo.items.services.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final CircuitBreakerFactory circuitBreakerFactory;

    @GetMapping
    public List<Item> findAll(@RequestParam(name = "name", required = false) String name,
                              @RequestHeader(name = "request-obligatory-example", required = false) String header) {
        log.info("name: {}", name);
        log.info("header: {}", header);
        return itemService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> findById(@PathVariable Long id) {
        Optional<Item> item = circuitBreakerFactory.create("item-service")
                .run(() -> itemService.findById(id), e -> {
                    ProductDto productDto = new ProductDto();
                    productDto.setId(id);
                    productDto.setCreateAt(LocalDate.now());
                    productDto.setName("Default product");
                    productDto.setPrice(525.0);
                    log.error("Error: {}", e.getMessage());
                    return Optional.of(new Item(productDto, 1));
                });
        if (item.isPresent()) {
            return new ResponseEntity<>(item.get(), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
    }
}
