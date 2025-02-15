package com.servicio.items.controllers;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.servicio.items.models.Item;
import com.servicio.items.models.Product;
import com.servicio.items.services.ItemService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ItemController {

    private final ItemService service;

    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    public ItemController(@Qualifier("itemServiceWebClient") ItemService service,
	    CircuitBreakerFactory<?, ?> circuitBreakerFactory) {
	this.service = service;
	this.circuitBreakerFactory = circuitBreakerFactory;

    }

    @GetMapping
    public List<Item> list(@RequestParam(required = false) String name,
	    @RequestHeader(name = "token-request", required = false) String token) {
	log.info(name);
	log.info(token);

	return service.findAll();

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> details(@PathVariable Long id) {
	Optional<Item> item = circuitBreakerFactory.create("items").run(() -> this.service.findById(id), e -> {

	    log.error(String.format("Error %s, [%s]", e.getMessage(), e));

	    Product product = new Product();
	    product.setCreateAt(LocalDate.now());
	    product.setId(1l);
	    product.setName("Camara");
	    product.setPrice(500.00);
	    return Optional.of(new Item(product, 5));
	});
	if (item.isPresent()) {
	    return ResponseEntity.status(HttpStatus.OK).body(item.get());

	}
	return ResponseEntity.status(404)
		.body(Collections.singletonMap("message", String.format("Product with id %s doesn´t exist", id)));
    }

}
