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

import static com.demo.items.util.Constants.ID_PARAM;

@Service
@Primary
@RequiredArgsConstructor
public class ItemServiceWebClientImpl implements ItemService {

    private final WebClient.Builder webClient;
    private final Random random = new Random();

    @Override
    public List<Item> findAll() {

        return webClient.build()
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(ProductDto.class)
                .map(product -> new Item(product, random.nextInt(10) + 1))
                .collectList()
                .block();
    }

    @Override
    public Optional<Item> findById(Long id) {
        Map<String, Long> params = new HashMap<>();
        params.put("id", id);
        return Optional.ofNullable(webClient.build()
                .get()
                .uri(ID_PARAM, params)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .map(product -> new Item(product, random.nextInt(10) + 1))
                .block());

    }

    @Override
    public ProductDto save(ProductDto productDto) {
        return webClient.build()
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productDto)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();
    }

    @Override
    public ProductDto update(Long id, ProductDto productDto) {
        try {
            return webClient.build()
                    .put()
                    .uri(ID_PARAM, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(productDto)
                    .retrieve()
                    .bodyToMono(ProductDto.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            webClient.build()
                    .delete()
                    .uri(ID_PARAM, id)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
}
