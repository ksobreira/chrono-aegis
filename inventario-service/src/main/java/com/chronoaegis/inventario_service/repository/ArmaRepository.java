package com.chronoaegis.inventario_service.repository;

import com.chronoaegis.inventario_service.model.Arma;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArmaRepository extends JpaRepository<Arma, Long> {
}
