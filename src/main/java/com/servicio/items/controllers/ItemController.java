package com.servicio.items.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.servicio.items.models.Item;
import com.servicio.items.services.ItemService;

@RestController
public class ItemController {

    private final ItemService service;

    public ItemController(@Qualifier("itemServiceWebClient") ItemService service) {
	this.service = service;

    }

    @GetMapping
    public List<Item> list() {
	return service.findAll();

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> details(@PathVariable Long id) {
	Optional<Item> item = this.service.findById(id);
	if (item.isPresent()) {
	    return ResponseEntity.status(HttpStatus.OK).body(item.get());

	}
	return ResponseEntity.status(404)
		.body(Collections.singletonMap("message", String.format("Product with id %s doesn´t exist", id)));
    }

}
