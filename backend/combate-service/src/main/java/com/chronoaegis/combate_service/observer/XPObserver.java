package com.chronoaegis.combate_service.observer;

import com.chronoaegis.combate_service.client.PersonagemClient;
import com.chronoaegis.combate_service.model.BatalhaSession;

public class XPObserver implements BatalhaObserver {

    private final PersonagemClient personagemClient;

    public XPObserver(PersonagemClient personagemClient) {
        this.personagemClient = personagemClient;
    }

    @Override
    public void notificar(BatalhaSession sessao, String evento) {
        if ("VITORIA".equals(evento)) {
            int xp = calcularXp(sessao.getMonstro().getNome());
            personagemClient.ganharXp(sessao.getPersonagemId(), xp);
        }
    }

    private int calcularXp(String nomeMonstro) {
        return switch (nomeMonstro) {
            case "Goblin" -> 30;
            case "Ogro" -> 60;
            case "Dragão" -> 100;
            default -> 20;
        };
    }
}
