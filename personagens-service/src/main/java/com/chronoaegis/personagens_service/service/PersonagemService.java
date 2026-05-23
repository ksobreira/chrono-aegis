package com.chronoaegis.personagens_service.service;

import com.chronoaegis.personagens_service.dto.PersonagemDTO;
import com.chronoaegis.personagens_service.enums.Arquetipo;
import com.chronoaegis.personagens_service.factory.*;
import com.chronoaegis.personagens_service.model.Personagem;
import com.chronoaegis.personagens_service.repository.PersonagemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonagemService {
    private final PersonagemRepository repository;
    public PersonagemService(PersonagemRepository repository) {
        this.repository = repository;
    }
    public Personagem criarPersonagem(PersonagemDTO dto) {
        PersonagemFactory factory;

        switch (dto.getClasse().toUpperCase()) {
            case "GUERREIRO": factory = new GuerreiroFactory(); break;
            case "MAGO": factory = new MagoFactory(); break;
            case "ARQUEIRO": factory = new ArqueiroFactory(); break;
            case "CLERIGO": factory = new ClerigoFactory(); break;
            default: throw new IllegalArgumentException("Classe base desconhecida.");
        }

        Personagem novo = factory.criar(dto.getUsuarioId(), dto.getNome());
        return repository.save(novo);
    }

    public List<Personagem> buscarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    public Personagem ganharXp(Long personagemId, int xpGanho) {
        Personagem p = repository.findById(personagemId)
                .orElseThrow(() -> new RuntimeException("Personagem não encontrado"));

        p.setXp(p.getXp() + xpGanho);
        while (p.getXp() >= 100 && p.getNivel() < 4) {
            p.setXp(p.getXp() - 100); // Deduz os 100 de XP usados para upar
            p.setNivel(p.getNivel() + 1);
            subirNivel(p); // Executa a trigger de atributos
        }
        return repository.save(p);
    }
    private void subirNivel(Personagem p) {
        switch (p.getClasseBase()) {
            case GUERREIRO:
                p.setVidaMax(p.getVidaMax() + 20);
                p.setAtaque(p.getAtaque() + 4);
                p.setDefesa(p.getDefesa() + 2);
                break;
            case MAGO:
                p.setVidaMax(p.getVidaMax() + 10);
                p.setAtaque(p.getAtaque() + 6);
                p.setDefesa(p.getDefesa() + 1);
                break;
            case ARQUEIRO:
                p.setVidaMax(p.getVidaMax() + 15);
                p.setAtaque(p.getAtaque() + 5);
                p.setDefesa(p.getDefesa() + 1);
                break;
            case CLERIGO:
                p.setVidaMax(p.getVidaMax() + 15);
                p.setAtaque(p.getAtaque() + 3);
                p.setDefesa(p.getDefesa() + 2);
                break;
        }
    }
    public Personagem evoluirParaArquetipo(Long personagemId, String escolhaArquetipo) {
        Personagem p = repository.findById(personagemId)
                .orElseThrow(() -> new RuntimeException("Personagem não encontrado"));

        Arquetipo novoArquetipo = Arquetipo.valueOf(escolhaArquetipo.toUpperCase());
        p.setArquetipo(novoArquetipo);
        p.setNivel(5);
        p.setXp(0);

        switch (novoArquetipo) {
            // guerreiro
            case PALADINO: p.setVidaMax(250); p.setAtaque(40); p.setDefesa(30); break;
            case BERSERKER: p.setVidaMax(200); p.setAtaque(55); p.setDefesa(20); break;

            // mago
            case FEITICEIRO: p.setVidaMax(115); p.setAtaque(85); p.setDefesa(8); break;
            case BATTLEMAGE: p.setVidaMax(140); p.setAtaque(65); p.setDefesa(18); break;

            // arqueiro
            case SNIPER: p.setVidaMax(150); p.setAtaque(65); p.setDefesa(12); break;
            case RANGER: p.setVidaMax(175); p.setAtaque(50); p.setDefesa(18); break;

            // clérigo
            case SACERDOTE: p.setVidaMax(160); p.setAtaque(30); p.setDefesa(20); break;
            case INQUISIDOR: p.setVidaMax(190); p.setAtaque(45); p.setDefesa(24); break;

            default:
                throw new IllegalArgumentException("Arquétipo inválido.");
        }

        return repository.save(p);
    }
}