package com.chronoaegis.personagens_service.service;
import com.chronoaegis.personagens_service.dto.PersonagemDTO;
import com.chronoaegis.personagens_service.factory.*;
import com.chronoaegis.personagens_service.model.Personagem;
import com.chronoaegis.personagens_service.repository.PersonagemRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class PersonagemService {
    private final PersonagemRepository repository;

    public PersonagemService(PersonagemRepository repository){
        this.repository = repository;
    }
    public Personagem criarPersonagem(PersonagemDTO dto){
        PersonagemFactory factory;

        switch (dto.getClasse().toUpperCase()){
            case "GUERREIRO": factory = new GuerreiroFactory(); break;
            case "MAGO": factory = new MagoFactory(); break;
            case "ARQUEIRO": factory = new ArqueiroFactory(); break;
            case "CLERIGO": factory = new ClerigoFactory(); break;
            default: throw new IllegalArgumentException("Classe desconhecida!");
        }
        Personagem novo = factory.criar(dto.getUsuarioId(), dto.getNome());
        return repository.save(novo);
    }

    public List<Personagem> buscarFichas(Long usuarioId){
        return repository.findByUsuarioId(usuarioId);
    }
}
