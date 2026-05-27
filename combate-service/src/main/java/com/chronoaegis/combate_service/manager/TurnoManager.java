package com.chronoaegis.combate_service.manager;

import com.chronoaegis.combate_service.dto.ResultadoTurnoDTO;
import com.chronoaegis.combate_service.model.BatalhaSession;
import com.chronoaegis.combate_service.state.EstadoMorto;
import org.springframework.stereotype.Component;

@Component
public class TurnoManager {

    public ResultadoTurnoDTO executarTurnoInimigo(BatalhaSession sessao) {
        // 1. Calcula o dano do monstro
        int dano = Math.max(1, sessao.getMonstro().getAtaque() - sessao.getDefesaPersonagem());

        // 2. Aplica o dano no hpAtualPersonagem da sessão
        sessao.setHpAtualPersonagem(sessao.getHpAtualPersonagem() - dano);

        // 3. Verifica se o personagem chegou a 0 HP e atualiza EstadoPersonagem para EstadoMorto
        if (sessao.getHpAtualPersonagem() <= 0) {
            sessao.setHpAtualPersonagem(0);
            sessao.setEstadoPersonagem(new EstadoMorto());
        }

        // 4. Registra o evento no log
        sessao.getLog().add("Inimigo atacou! Dano: " + dano + " | HP Jogador: " + sessao.getHpAtualPersonagem());

        ResultadoTurnoDTO resultado = new ResultadoTurnoDTO();
        resultado.setHpJogador(sessao.getHpAtualPersonagem());
        resultado.setHpMonstro(sessao.getMonstro().getHpAtual());
        resultado.setTurnoAtual("PROCESSANDO_EFEITOS");
        resultado.setLog(sessao.getLog());
        resultado.setFimDeBatalha(false);

        return resultado;
    }
}
