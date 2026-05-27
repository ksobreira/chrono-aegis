package com.chronoaegis.inventario_service.service;

import com.chronoaegis.inventario_service.model.Item;
import com.chronoaegis.inventario_service.repository.ItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item criar(Item item) {
        return itemRepository.save(item);
    }

    public List<Item> buscarPorPersonagem(Long personagemId) {
        return itemRepository.findByPersonagemId(personagemId);
    }

    public Item buscarPorId(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));
    }

    public void deletar(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new RuntimeException("Item não encontrado");
        }
        itemRepository.deleteById(id);
    }
}