package com.chronoaegis.personagens_service.dto;

public class PersonagemDTO {
    private Long usuarioId;
    private String nome;
    private String classe;

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getClasse() { return classe; }
    public void setClasse(String classe) { this.classe = classe; }
}
