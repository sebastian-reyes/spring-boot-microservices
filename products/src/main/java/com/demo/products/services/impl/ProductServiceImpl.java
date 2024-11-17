package com.demo.products.services.impl;

import com.demo.products.entities.Product;
import com.demo.products.repository.ProductRepository;
import com.demo.products.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final Environment env;

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return (productRepository.findAll()).stream()
                .peek(product -> product
                        .setPort(Integer.parseInt(Objects.requireNonNull(env.getProperty("local.server.port")))))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id).map(product -> {
            product.setPort(Integer.parseInt(Objects.requireNonNull(env.getProperty("local.server.port"))));
            return product;
        });
    }
}
