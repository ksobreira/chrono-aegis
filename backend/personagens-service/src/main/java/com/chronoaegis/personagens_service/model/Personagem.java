package com.chronoaegis.personagens_service.model;

import com.chronoaegis.personagens_service.enums.Arquetipo;
import com.chronoaegis.personagens_service.enums.ClassePersonagem;
import com.chronoaegis.personagens_service.enums.CombatPosition;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Personagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;
    private String nome;

    @Enumerated(EnumType.STRING)
    private ClassePersonagem classeBase;

    @Enumerated(EnumType.STRING)
    private Arquetipo arquetipo = Arquetipo.NENHUM;

    private int nivel = 1;
    private int xp = 0;
    private int vidaMax;
    private int ataque;
    private int defesa;

    @Enumerated(EnumType.STRING)
    private CombatPosition posicao;

    // Getters e Setters básicos
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public ClassePersonagem getClasseBase() { return classeBase; }
    public void setClasseBase(ClassePersonagem classeBase) { this.classeBase = classeBase; }
    public Arquetipo getArquetipo() { return arquetipo; }
    public void setArquetipo(Arquetipo arquetipo) { this.arquetipo = arquetipo; }
    public int getNivel() { return nivel; }
    public void setNivel(int nivel) { this.nivel = nivel; }
    public int getXp() { return xp; }
    public void setXp(int xp) { this.xp = xp; }
    public int getVidaMax() { return vidaMax; }
    public void setVidaMax(int vidaMax) { this.vidaMax = vidaMax; }
    public int getAtaque() { return ataque; }
    public void setAtaque(int ataque) { this.ataque = ataque; }
    public int getDefesa() { return defesa; }
    public void setDefesa(int defesa) { this.defesa = defesa; }
    public CombatPosition getPosicao() { return posicao; }
    public void setPosicao(CombatPosition posicao) { this.posicao = posicao; }
}