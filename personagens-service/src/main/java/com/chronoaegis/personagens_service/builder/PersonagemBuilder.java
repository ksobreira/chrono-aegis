package com.chronoaegis.personagens_service.builder;
import com.chronoaegis.personagens_service.enums.ClassePersonagem;
import com.chronoaegis.personagens_service.enums.CombatPosition;
import com.chronoaegis.personagens_service.model.Personagem;
public class PersonagemBuilder {
    private Personagem personagem;
    public PersonagemBuilder(Personagem personagem){
        this.personagem = personagem;
    }

    public PersonagemBuilder usuarioId(Long id){
        this.personagem.setUsuarioId(id);
        return this;
    }

    public PersonagemBuilder nome(String nome){
        this.personagem.setNome(nome);
        return this;
    }

    public PersonagemBuilder classeBase(ClassePersonagem classe){
        this.personagem.setClasseBase(classe);
        return this;
    }

    public PersonagemBuilder vida(int vida){
        this.personagem.setVidaMax(vida);
        return this;
    }

    public PersonagemBuilder ataque(int ataque){
        this.personagem.setAtaque(ataque);
        return this;
    }

    public PersonagemBuilder defesa(int defesa){
        this.personagem.setDefesa(defesa);
        return this;
    }

    public PersonagemBuilder posicao (CombatPosition posicao){
        this.personagem.setPosicao(posicao);
        return this;
    }

    public Personagem build(){
        return this.personagem;
    }
}
