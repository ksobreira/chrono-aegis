package com.chronoaegis.combate_service.model;

import com.chronoaegis.combate_service.manager.TurnoManager;
import com.chronoaegis.combate_service.observer.BatalhaObserver;
import com.chronoaegis.combate_service.state.EstadoCombate;
import com.chronoaegis.combate_service.state.EstadoPersonagem;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class BatalhaSession {
    private UUID id;
    private Long personagemId;
    private String nomePersonagem;
    private int hpAtualPersonagem;
    private int hpMaxPersonagem;
    private int ataquePersonagem;
    private int defesaPersonagem;
    private String posicao;
    private String tagArma;
    private int danoArma;
    private Monstro monstro;
    private TurnoManager turnoManager;
    private EstadoCombate turnoAtual;
    private EstadoPersonagem estadoPersonagem;
    private List<BatalhaObserver> observers = new ArrayList<>();
    private List<String> log = new ArrayList<>();
    private boolean encerrada;

    public void notificarObservers(String evento) {
        observers.forEach(o -> o.notificar(this, evento));
    }
}
