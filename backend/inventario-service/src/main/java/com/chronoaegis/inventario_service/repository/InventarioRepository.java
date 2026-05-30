package com.chronoaegis.inventario_service.repository;

import com.chronoaegis.inventario_service.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    Optional<Inventario> findByPersonagemId(long personagemId);
}
