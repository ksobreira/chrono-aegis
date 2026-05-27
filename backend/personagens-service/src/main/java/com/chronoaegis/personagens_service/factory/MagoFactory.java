package com.chronoaegis.personagens_service.factory;

import com.chronoaegis.personagens_service.builder.PersonagemBuilder;
import com.chronoaegis.personagens_service.enums.ClassePersonagem;
import com.chronoaegis.personagens_service.enums.CombatPosition;
import com.chronoaegis.personagens_service.model.Mago;
import com.chronoaegis.personagens_service.model.Personagem;

public class MagoFactory implements PersonagemFactory {
    @Override
    public Personagem criar(Long usuarioId, String nome) {
        return new PersonagemBuilder(new Mago())
                .usuarioId(usuarioId)
                .nome(nome)
                .classeBase(ClassePersonagem.MAGO)
                .vidaMax(75)
                .ataque(40)
                .defesa(5)
                .posicao(CombatPosition.RETAGUARDA)
                .build();
    }
}