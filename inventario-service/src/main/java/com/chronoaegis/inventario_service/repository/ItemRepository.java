package com.chronoaegis.inventario_service.repository;

import com.chronoaegis.inventario_service.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByPersonagemId(long personagemId);
}
