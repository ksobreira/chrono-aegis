package com.chronoaegis.inventario_service.service;

import com.chronoaegis.inventario_service.decorator.*;
import com.chronoaegis.inventario_service.dto.EquiparDTO;
import com.chronoaegis.inventario_service.enums.WeaponTag;
import com.chronoaegis.inventario_service.model.Arma;
import com.chronoaegis.inventario_service.model.Inventario;
import com.chronoaegis.inventario_service.repository.ArmaRepository;
import com.chronoaegis.inventario_service.repository.InventarioRepository;
import org.springframework.stereotype.Service;

@Service
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final ArmaRepository armaRepository;

    public InventarioService(InventarioRepository inventarioRepository, ArmaRepository armaRepository) {
        this.inventarioRepository = inventarioRepository;
        this.armaRepository = armaRepository;
    }

    public Inventario buscarOuCriar(long personagemId){
        return inventarioRepository.findByPersonagemId(personagemId)
                .orElseGet(() -> {
                   Inventario inv = new Inventario();
                   inv.setPersonagemId(personagemId);
                   return inventarioRepository.save(inv);
                });
    }

    public Inventario equipar(EquiparDTO dto){
        Arma arma = armaRepository.findById(dto.getArmaId())
                .orElseThrow(() -> new RuntimeException("Arma não encontrada"));

        WeaponComponent weapon = new BaseWeapon(arma.getNome(), arma.getDanoBase());

        for (WeaponTag tag : arma.getTags()){
            switch (tag){
                case PESADA -> weapon = new HeavyWeaponDecorator(weapon);
                case RECARGA -> weapon = new LoadingWeaponDecorator(weapon);
                case ACUIDADE -> weapon = new FinesseWeaponDecorator(weapon, dto.getForcaDoPersonagem());
            }
        }

        if (!weapon.podeEquipar(dto.getForcaDoPersonagem())){
            throw new RuntimeException(
                    "Força insuficiente para equipar" + arma.getNome() + ". Mínimo necessário" + arma.getForcaMin()
            );
        }

        Inventario inv = buscarOuCriar(dto.getPersonagemId());
        if (!inv.getArmas().contains(arma)){
            inv.getArmas().add(arma);
        }
        inv.setArmaEquipada(arma);
        return inventarioRepository.save(inv);
    }

    public Inventario buscarPorPersonagem(Long personagemId){
        return buscarOuCriar(personagemId);
    }
}
