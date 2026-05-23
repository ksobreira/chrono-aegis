package com.chronoaegis.personagens_service.repository;

import com.chronoaegis.personagens_service.model.Personagem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PersonagemRepository extends JpaRepository<Personagem, Long> {
    List<Personagem> findByUsuarioId(Long usuarioId);
}