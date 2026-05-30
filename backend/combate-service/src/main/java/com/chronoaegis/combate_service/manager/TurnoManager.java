package com.chronoaegis.combate_service.manager;

import com.chronoaegis.combate_service.dto.ResultadoTurnoDTO;
import com.chronoaegis.combate_service.model.BatalhaSession;
import com.chronoaegis.combate_service.state.EstadoAtordoado;
import com.chronoaegis.combate_service.state.EstadoEnvenenado;
import com.chronoaegis.combate_service.state.EstadoMorto;
import com.chronoaegis.combate_service.state.EstadoVivo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class TurnoManager {

    private final Random random = new Random();

    public ResultadoTurnoDTO executarTurnoInimigo(BatalhaSession sessao) {
        List<String> logs = new ArrayList<>();

        // 1. Calcula dano do monstro
        int dano = Math.max(1, sessao.getMonstro().getAtaque() - sessao.getDefesaPersonagem());
        sessao.setHpAtualPersonagem(sessao.getHpAtualPersonagem() - dano);
        logs.add("Inimigo atacou! Dano: " + dano + " | HP Jogador: "
                + Math.max(0, sessao.getHpAtualPersonagem()));

        // 2. Aplica morte se HP zerou
        if (sessao.getHpAtualPersonagem() <= 0) {
            sessao.setHpAtualPersonagem(0);
            sessao.setEstadoPersonagem(new EstadoMorto());
        } else if (sessao.getEstadoPersonagem() instanceof EstadoVivo) {
            // 3. Chance de aplicar status (só se ainda estiver Vivo)
            double roll = random.nextDouble();
            if (roll < 0.20) {
                sessao.setEstadoPersonagem(new EstadoEnvenenado());
                logs.add("☠️ Você foi envenenado! Perderá 5 HP por turno.");
            } else if (roll < 0.35) {
                sessao.setEstadoPersonagem(new EstadoAtordoado());
                logs.add("💫 Você foi atordoado! Perderá seu próximo turno.");
            }
        }

        ResultadoTurnoDTO resultado = new ResultadoTurnoDTO();
        resultado.setHpJogador(sessao.getHpAtualPersonagem());
        resultado.setHpMonstro(sessao.getMonstro().getHpAtual());
        resultado.setTurnoAtual("PROCESSANDO_EFEITOS");
        resultado.setLog(logs);
        resultado.setFimDeBatalha(false);
        return resultado;
    }
}