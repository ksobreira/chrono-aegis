package com.chronoaegis.inventario_service.model;


import com.chronoaegis.inventario_service.enums.WeaponTag;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "armas")
public class Arma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nome;
    private int danoBase;
    private int forcaMin;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<WeaponTag> tags;

    private boolean isLoaded = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public int getDanoBase() {
        return danoBase;
    }

    public void setDanoBase(int danoBase) {
        this.danoBase = danoBase;
    }

    public int getForcaMin() {
        return forcaMin;
    }

    public void setForcaMin(int forcaMin) {
        this.forcaMin = forcaMin;
    }

    public List<WeaponTag> getTags() {
        return tags;
    }

    public void setTags(List<WeaponTag> tags) {
        this.tags = tags;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean isLoaded) {
        this.isLoaded = isLoaded;
    }

}
