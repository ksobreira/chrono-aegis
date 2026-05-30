package com.chronoaegis.combate_service.facade;

import com.chronoaegis.combate_service.client.InventarioClient;
import com.chronoaegis.combate_service.client.PersonagemClient;
import com.chronoaegis.combate_service.dto.ArmaResponseDTO;
import com.chronoaegis.combate_service.dto.InventarioResponseDTO;
import com.chronoaegis.combate_service.dto.PersonagemResponseDTO;
import com.chronoaegis.combate_service.factory.MonstroFactory;
import com.chronoaegis.combate_service.manager.TurnoManager;
import com.chronoaegis.combate_service.model.BatalhaSession;
import com.chronoaegis.combate_service.model.Monstro;
import com.chronoaegis.combate_service.observer.LogObserver;
import com.chronoaegis.combate_service.observer.XPObserver;
import com.chronoaegis.combate_service.state.EstadoVivo;
import com.chronoaegis.combate_service.state.TurnoJogadorState;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BatalhaFacade {

    private final PersonagemClient personagemClient;
    private final InventarioClient inventarioClient;
    private final TurnoManager turnoManager;

    public BatalhaFacade(PersonagemClient personagemClient,
                         InventarioClient inventarioClient,
                         TurnoManager turnoManager) {
        this.personagemClient = personagemClient;
        this.inventarioClient = inventarioClient;
        this.turnoManager = turnoManager;
    }

    public BatalhaSession iniciar(Long personagemId) {
        PersonagemResponseDTO personagem = personagemClient.buscarPorId(personagemId);
        InventarioResponseDTO inventario = inventarioClient.buscarPorPersonagem(personagemId);

        ArmaResponseDTO arma = inventario.getArmaEquipada();
        int danoArma;
        String tagArma;
        if (arma == null) {
            danoArma = 5;
            tagArma = "NORMAL";
        } else {
            danoArma = arma.getDanoBase();
            tagArma = arma.getTags().isEmpty() ? "NORMAL" : arma.getTags().get(0);
        }

        Monstro monstro = MonstroFactory.gerar(personagem.getNivel());

        BatalhaSession sessao = new BatalhaSession();
        sessao.setId(UUID.randomUUID());
        sessao.setPersonagemId(personagemId);
        sessao.setNomePersonagem(personagem.getNome());
        sessao.setHpAtualPersonagem(personagem.getVidaMax());
        sessao.setHpMaxPersonagem(personagem.getVidaMax());
        sessao.setAtaquePersonagem(personagem.getAtaque());
        sessao.setDefesaPersonagem(personagem.getDefesa());
        sessao.setPosicao(personagem.getPosicao());
        sessao.setTagArma(tagArma);
        sessao.setDanoArma(danoArma);
        sessao.setMonstro(monstro);
        sessao.setTurnoManager(turnoManager);
        sessao.setTurnoAtual(new TurnoJogadorState());
        sessao.setEstadoPersonagem(new EstadoVivo());
        sessao.setEncerrada(false);

        sessao.getObservers().add(new LogObserver());
        sessao.getObservers().add(new XPObserver(personagemClient));

        sessao.getLog().add("Batalha iniciada! " + personagem.getNome() + " vs " + monstro.getNome());

        return sessao;
    }
}
