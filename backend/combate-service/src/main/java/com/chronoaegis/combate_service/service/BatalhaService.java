package com.chronoaegis.combate_service.service;

import com.chronoaegis.combate_service.command.AtacarCommand;
import com.chronoaegis.combate_service.command.ComandoBatalha;
import com.chronoaegis.combate_service.command.FugirCommand;
import com.chronoaegis.combate_service.command.UsarItemCommand;
import com.chronoaegis.combate_service.dto.AcaoDTO;
import com.chronoaegis.combate_service.dto.BatalhaResponseDTO;
import com.chronoaegis.combate_service.dto.IniciarBatalhaDTO;
import com.chronoaegis.combate_service.dto.ResultadoTurnoDTO;
import com.chronoaegis.combate_service.facade.BatalhaFacade;
import com.chronoaegis.combate_service.model.BatalhaSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class BatalhaService {

    private final BatalhaFacade batalhaFacade;
    // HashMap em memória para armazenar sessões ativas
    private final Map<UUID, BatalhaSession> sessoes = new HashMap<>();

    public BatalhaService(BatalhaFacade batalhaFacade) {
        this.batalhaFacade = batalhaFacade;
    }

    public BatalhaResponseDTO iniciarBatalha(IniciarBatalhaDTO dto) {
        BatalhaSession sessao = batalhaFacade.iniciar(dto.getPersonagemId());
        sessoes.put(sessao.getId(), sessao);

        BatalhaResponseDTO response = new BatalhaResponseDTO();
        response.setSessionId(sessao.getId());
        response.setNomePersonagem(sessao.getNomePersonagem());
        response.setHpPersonagem(sessao.getHpAtualPersonagem());
        response.setNomeMonstro(sessao.getMonstro().getNome());
        response.setHpMonstro(sessao.getMonstro().getHpAtual());
        response.setTurnoAtual("TURNO_JOGADOR");
        return response;
    }

    public ResultadoTurnoDTO processarAcao(AcaoDTO dto) {
        BatalhaSession sessao = sessoes.get(dto.getSessionId());
        if (sessao == null) {
            throw new RuntimeException("Sessão de batalha não encontrada: " + dto.getSessionId());
        }

        ComandoBatalha comando = switch (dto.getTipoAcao()) {
            case ATACAR -> new AtacarCommand();
            case FUGIR -> new FugirCommand();
            case USAR_ITEM -> new UsarItemCommand();
        };

        return sessao.getTurnoAtual().processar(sessao, comando);
    }
}
