package com.servicio.items.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.servicio.items.models.Item;
import com.servicio.items.services.ItemService;

@RestController
public class ItemController {

    private final ItemService service;

    public ItemController(ItemService service) {
	this.service = service;

    }

    @GetMapping
    public List<Item> list() {
	return service.findAll();

    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> details(@PathVariable Long id) {
	Optional<Item> item = this.service.findById(id);
	if (item.isPresent()) {
	    return ResponseEntity.ok(item.get());

	}
	return ResponseEntity.notFound().build();

    }

}
