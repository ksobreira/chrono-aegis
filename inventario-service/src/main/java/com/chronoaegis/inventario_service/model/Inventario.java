package com.chronoaegis.inventario_service.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inventarios")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long personagemId;

    @ManyToMany
    private List<Arma> armas = new ArrayList<>();

    @ManyToOne
    private Arma armaEquipada;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPersonagemId() {
        return personagemId;
    }

    public void setPersonagemId(Long personagemId) {
        this.personagemId = personagemId;
    }

    public List<Arma> getArmas() {
        return armas;
    }

    public void setArmas(List<Arma> armas) {
        this.armas = armas;
    }

    public Arma getArmaEquipada() {
        return armaEquipada;
    }

    public void setArmaEquipada(Arma armaEquipada) {
        this.armaEquipada = armaEquipada;
    }
}
