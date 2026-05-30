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
        PersonagemFactory factory = switch (dto.getClasse().toUpperCase()) {
            case "GUERREIRO" -> new GuerreiroFactory();
            case "MAGO"      -> new MagoFactory();
            case "ARQUEIRO"  -> new ArqueiroFactory();
            case "CLERIGO"   -> new ClerigoFactory();
            default -> throw new IllegalArgumentException("Classe desconhecida: " + dto.getClasse());
        };
        return repository.save(factory.criar(dto.getUsuarioId(), dto.getNome()));
    }

    public List<Personagem> buscarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    // ADICIONADO: necessário para o combate-service buscar via Feign
    public Personagem buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Personagem não encontrado: " + id));
    }

    public Personagem ganharXp(Long personagemId, int xpGanho) {
        Personagem p = buscarPorId(personagemId);
        p.setXp(p.getXp() + xpGanho);
        // Nível máximo antes de evoluir é 4, evoluindo vai para 5
        while (p.getXp() >= 100 && p.getNivel() < 4) {
            p.setXp(p.getXp() - 100);
            p.setNivel(p.getNivel() + 1);
            subirNivel(p);
        }
        return repository.save(p);
    }

    private void subirNivel(Personagem p) {
        switch (p.getClasseBase()) {
            case GUERREIRO -> { p.setVidaMax(p.getVidaMax() + 20); p.setAtaque(p.getAtaque() + 4); p.setDefesa(p.getDefesa() + 2); }
            case MAGO      -> { p.setVidaMax(p.getVidaMax() + 10); p.setAtaque(p.getAtaque() + 6); p.setDefesa(p.getDefesa() + 1); }
            case ARQUEIRO  -> { p.setVidaMax(p.getVidaMax() + 15); p.setAtaque(p.getAtaque() + 5); p.setDefesa(p.getDefesa() + 1); }
            case CLERIGO   -> { p.setVidaMax(p.getVidaMax() + 15); p.setAtaque(p.getAtaque() + 3); p.setDefesa(p.getDefesa() + 2); }
        }
    }

    public Personagem evoluirParaArquetipo(Long personagemId, String escolhaArquetipo) {
        Personagem p = buscarPorId(personagemId);
        if (p.getNivel() < 4) {
            throw new RuntimeException("Personagem precisa ser nível 4 para evoluir");
        }
        Arquetipo novoArquetipo = Arquetipo.valueOf(escolhaArquetipo.toUpperCase());
        p.setArquetipo(novoArquetipo);
        p.setNivel(5);
        p.setXp(0);
        switch (novoArquetipo) {
            case PALADINO   -> { p.setVidaMax(250); p.setAtaque(40); p.setDefesa(30); }
            case BERSERKER  -> { p.setVidaMax(200); p.setAtaque(55); p.setDefesa(20); }
            case FEITICEIRO -> { p.setVidaMax(115); p.setAtaque(85); p.setDefesa(8);  }
            case BATTLEMAGE -> { p.setVidaMax(140); p.setAtaque(65); p.setDefesa(18); }
            case SNIPER     -> { p.setVidaMax(150); p.setAtaque(65); p.setDefesa(12); }
            case RANGER     -> { p.setVidaMax(175); p.setAtaque(50); p.setDefesa(18); }
            case SACERDOTE  -> { p.setVidaMax(160); p.setAtaque(30); p.setDefesa(20); }
            case INQUISIDOR -> { p.setVidaMax(190); p.setAtaque(45); p.setDefesa(24); }
            default -> throw new IllegalArgumentException("Arquétipo inválido: " + escolhaArquetipo);
        }
        return repository.save(p);
    }
}
