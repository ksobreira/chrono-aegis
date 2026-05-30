package com.chronoaegis.personagens_service.factory;

import com.chronoaegis.personagens_service.builder.PersonagemBuilder;
import com.chronoaegis.personagens_service.enums.ClassePersonagem;
import com.chronoaegis.personagens_service.enums.CombatPosition;
import com.chronoaegis.personagens_service.model.Guerreiro;
import com.chronoaegis.personagens_service.model.Personagem;

public class GuerreiroFactory implements PersonagemFactory {
    @Override
    public Personagem criar(Long usuarioId, String nome) {
        return new PersonagemBuilder(new Guerreiro())
                .usuarioId(usuarioId)
                .nome(nome)
                .classeBase(ClassePersonagem.GUERREIRO)
                .vidaMax(120)
                .ataque(25)
                .defesa(15)
                .posicao(CombatPosition.VANGUARDA)
                .build();
    }
}