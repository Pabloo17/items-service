package com.servicio.items.services;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.servicio.items.clients.ProductFeignClient;
import com.servicio.items.models.Item;
import com.servicio.items.models.Product;

@Service
public class ItemServiceFeign implements ItemService {

    private final ProductFeignClient client;

    private final Random random;

    public ItemServiceFeign(ProductFeignClient client) {
	this.client = client;
	this.random = new Random();
    }

    @Override
    public List<Item> findAll() {

	return client.findAll().stream().map(product -> new Item(product, this.random.nextInt(10) + 1)).toList();

    }

    @Override
    public Optional<Item> findById(Long id) {
	Product product = client.details(id);
	if (product == null) {
	    return Optional.empty();

	}
	return Optional.ofNullable(new Item(product, this.random.nextInt(10) + 1));
    }

}
