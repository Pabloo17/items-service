package com.servicio.items.services;

import java.util.List;
import java.util.Optional;

import com.servicio.items.models.Item;

public interface ItemService {

    List<Item> findAll();

    Optional<Item> findById(Long id);

}
