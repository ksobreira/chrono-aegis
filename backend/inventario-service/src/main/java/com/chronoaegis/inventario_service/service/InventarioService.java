package com.chronoaegis.inventario_service.service;

import com.chronoaegis.inventario_service.client.PersonagemClient;
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
    private final PersonagemClient personagemClient;

    public InventarioService(InventarioRepository inventarioRepository,
                             ArmaRepository armaRepository,
                             PersonagemClient personagemClient) {
        this.inventarioRepository = inventarioRepository;
        this.armaRepository = armaRepository;
        this.personagemClient = personagemClient;
    }

    public Inventario buscarOuCriar(Long personagemId) {
        return inventarioRepository.findByPersonagemId(personagemId)
                .orElseGet(() -> {
                    Inventario inv = new Inventario();
                    inv.setPersonagemId(personagemId);
                    return inventarioRepository.save(inv);
                });
    }

    public Inventario equipar(EquiparDTO dto) {
        Arma arma = armaRepository.findById(dto.getArmaId())
                .orElseThrow(() -> new RuntimeException("Arma não encontrada"));

        int forca = personagemClient.buscarForcaDoPersonagem(dto.getPersonagemId());

        WeaponComponent weapon = new BaseWeapon(arma.getNome(), arma.getDanoBase());
        for (WeaponTag tag : arma.getTags()) {
            weapon = switch (tag) {
                case PESADA    -> new HeavyWeaponDecorator(weapon);
                case RECARGA   -> new LoadingWeaponDecorator(weapon);
                case ACUIDADE  -> new FinesseWeaponDecorator(weapon, forca);
                default        -> weapon;
            };
        }

        if (!weapon.podeEquipar(forca)) {
            throw new RuntimeException("Força insuficiente para equipar " + arma.getNome()
                    + ". Mínimo necessário: " + arma.getForcaMin());
        }

        Inventario inv = buscarOuCriar(dto.getPersonagemId());
        if (!inv.getArmas().contains(arma)) {
            inv.getArmas().add(arma);
        }
        inv.setArmaEquipada(arma);
        return inventarioRepository.save(inv);
    }

    public Inventario buscarPorPersonagem(Long personagemId) {
        return buscarOuCriar(personagemId);
    }
}
