package com.chronoaegis.personagens_service.factory;

import com.chronoaegis.personagens_service.builder.PersonagemBuilder;
import com.chronoaegis.personagens_service.enums.ClassePersonagem;
import com.chronoaegis.personagens_service.enums.CombatPosition;
import com.chronoaegis.personagens_service.model.Arqueiro;
import com.chronoaegis.personagens_service.model.Personagem;

public class ArqueiroFactory implements PersonagemFactory {
    @Override
    public Personagem criar(Long usuarioId, String nome) {
        return new PersonagemBuilder(new Arqueiro())
                .usuarioId(usuarioId)
                .nome(nome)
                .classeBase(ClassePersonagem.ARQUEIRO)
                .vidaMax(90)
                .ataque(30)
                .defesa(10)
                .posicao(CombatPosition.RETAGUARDA)
                .build();
    }
}