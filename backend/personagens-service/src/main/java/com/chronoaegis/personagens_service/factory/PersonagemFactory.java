package com.chronoaegis.personagens_service.factory;
import com.chronoaegis.personagens_service.model.Personagem;

public interface PersonagemFactory {
    Personagem criar(Long usuarioId, String nome);
}
