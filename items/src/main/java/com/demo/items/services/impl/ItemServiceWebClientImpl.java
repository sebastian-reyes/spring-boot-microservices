package com.demo.items.services.impl;

import com.demo.items.models.Item;
import com.demo.items.models.ProductDto;
import com.demo.items.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.*;

@Service
@Primary
@RequiredArgsConstructor
public class ItemServiceWebClientImpl implements ItemService {

    private final WebClient.Builder webClient;

    @Override
    public List<Item> findAll() {
        return webClient.build()
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(ProductDto.class)
                .map(product -> new Item(product, new Random().nextInt(10) + 1))
                .collectList()
                .block();
    }

    @Override
    public Optional<Item> findById(Long id) {
        Map<String, Long> params = new HashMap<>();
        params.put("id", id);
        // try {
        return Optional.ofNullable(webClient.build()
                .get()
                .uri("/{id}", params)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .map(product -> new Item(product, new Random().nextInt(10) + 1))
                .block());
        // } catch (WebClientResponseException e) {
        //     return Optional.empty();
        // }
    }
}
