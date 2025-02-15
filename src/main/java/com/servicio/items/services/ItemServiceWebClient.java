package com.servicio.items.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import com.servicio.items.models.Item;
import com.servicio.items.models.Product;

// @Primary
@Service
public class ItemServiceWebClient implements ItemService {

    private final WebClient.Builder client;

    private final Random random;

    public ItemServiceWebClient(Builder client) {
	this.client = client;
	this.random = new Random();
    }

    @Override
    public List<Item> findAll() {
	return this.client.build().get().accept(MediaType.APPLICATION_JSON).retrieve().bodyToFlux(Product.class)
		.map(product -> new Item(product, this.random.nextInt(10) + 1)).collectList().block();
    }

    @Override
    public Optional<Item> findById(Long id) {
	Map<String, Long> params = new HashMap<>();
	params.put("id", id);

	// try {
	return Optional.ofNullable(client.build().get().uri("/{id}", params).accept(MediaType.APPLICATION_JSON)
		.retrieve().bodyToMono(Product.class).map(product -> new Item(product, this.random.nextInt(10) + 1))
		.block());

	// } catch (WebClientException e) {
	// return Optional.empty();
	// }

    }

}
