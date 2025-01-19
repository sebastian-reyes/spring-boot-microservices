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

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final Environment env;

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        List<Product> products = productRepository.findAll();
        String port = Objects.requireNonNull(env.getProperty("local.server.port"));
        for (Product product : products) {
            product.setPort(Integer.parseInt(port));
        }
        return products;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id).map(product -> {
            product.setPort(Integer.parseInt(Objects.requireNonNull(env.getProperty("local.server.port"))));
            return product;
        });
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new IllegalStateException("Product with id " + id + " not found");
        }
    }
}
