package com.chronoaegis.personagens_service.factory;

import com.chronoaegis.personagens_service.builder.PersonagemBuilder;
import com.chronoaegis.personagens_service.enums.ClassePersonagem;
import com.chronoaegis.personagens_service.enums.CombatPosition;
import com.chronoaegis.personagens_service.model.Clerigo;
import com.chronoaegis.personagens_service.model.Personagem;

public class ClerigoFactory implements PersonagemFactory {
    @Override
    public Personagem criar(Long usuarioId, String nome) {
        return new PersonagemBuilder(new Clerigo())
                .usuarioId(usuarioId)
                .nome(nome)
                .classeBase(ClassePersonagem.CLERIGO)
                .vidaMax(100)
                .ataque(20)
                .defesa(12)
                .posicao(CombatPosition.VANGUARDA)
                .build();
    }
}