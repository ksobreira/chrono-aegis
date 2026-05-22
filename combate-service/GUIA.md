# ⚔️ Microsserviço: `combate-service`
*(Responsáveis pela Implementação: ---)*

## 🎯 Objetivo do Serviço
Este microsserviço funciona como a "engine" (motor) principal de gameplay do RPG. Ele processa os encontros táticos por turnos entre o jogador e as ameaças do sistema. O combate é dinâmico: este serviço lê a posição do herói (Vanguarda/Retaguarda) e as propriedades das armas para aplicar fórmulas táticas de dano e gerenciar o fluxo rígido da rodada.

---

## 📝 Checklist de Implementação
- [ ] **Integração OpenFeign:** Configurar os clients HTTP (`PersonagemClient` e `InventarioClient`) para buscar em tempo real os status da ficha do herói e o dano das armas com seus modificadores.
- [ ] **Endpoints de Controle (`BatalhaController`):**
    - `POST /combate/iniciar` -> Aciona a Facade para carregar o personagem, gerar o monstro e abrir a sessão de batalha.
    - `POST /combate/acao` -> Recebe comandos de turno (Atacar, Fugir, Usar Item) encapsulados via Command.
- [ ] **Padrão GoF - State:** Implementar a Máquina de Estados Finitos (FSM) para ditar rigorosamente de quem é a vez e controlar o ciclo da batalha.
- [ ] **Padrão GoF - Strategy:** Implementar os algoritmos intercambiáveis de cálculo de dano com base na posição tática do alvo.
- [ ] **Padrões GoF - Facade + Command:**
    - **Facade:** Simplificar o setup inicial da batalha (orquestração de clients).
    - **Command:** Isolar a execução de cada ação do menu de combate (AtacarCommand, UsarItemCommand).
- Obs: Se houver mais endpoints que agregem no desenvolvimento pode adicionar
---

## 🗺️ Regra de Posição Tática (Vanguarda vs Retaguarda)

**Importante:** O `combate-service` **NÃO** guarda a posição física definitiva do personagem no banco.

1. O `personagem-service` define a posição padrão na criação (ex: Guerreiro nasce na Vanguarda).
2. O `combate-service` consome esse dado via Feign Client e injeta a String (`VANGUARDA` ou `RETAGUARDA`) no motor de batalha.
3. O padrão **Strategy** avalia essa String para aplicar o modificador correto de combate:

- **Estratégia Vanguarda:** Foco em combate corpo a corpo na linha de frente.  
  $$
  \text{Dano Final} = (\text{Ataque do Personagem} + \text{Dano da Arma}) \times 1.2 - \text{Defesa do Alvo}
  $$

- **Estratégia Retaguarda:** Foco em segurança recuada.  
  $$
  \text{Dano Final} = (\text{Ataque do Personagem} + \text{Dano da Arma}) \times 0.8 - \text{Defesa do Alvo}
  $$

---

## ⚙️ Máquina de Estados de Turno (State)

```
┌──────────────────┐       Ação Válida       ┌──────────────────┐
│  TURNO_JOGADOR   │ ──────────────────────> │  TURNO_INIMIGO   │
└──────────────────┘                         └──────────────────┘
        ▲                                             │
        │           HP de Ambos > 0                  │ IA Processada
        └─────────────────────────────────────────────▼
┌──────────────────┐
│PROCESSANDO_EFEITOS│
└──────────────────┘
        │
        │ HP de Alguém == 0
        ▼
┌──────────────────┐
│ FIM_DE_COMBATE   │
└──────────────────┘
```

---

## 💡 Exemplo Estrutural dos Padrões (State + Strategy)

```java
public interface EstrategiaDano {
    int calcular(int ataque, int danoArma, int defesaAlvo);
}

public class EstrategiaVanguarda implements EstrategiaDano {
    @Override
    public int calcular(int atq, int arma, int def) {
        int dano = (int) ((atq + arma) * 1.2) - def;
        return Math.max(1, dano);
    }
}

public class EstrategiaRetaguarda implements EstrategiaDano {
    @Override
    public int calcular(int atq, int arma, int def) {
        int dano = (int) ((atq + arma) * 0.8) - def;
        return Math.max(1, dano);
    }
}

public class ContextoBatalha {
    private EstadoCombate estadoAtual = new TurnoJogadorState();
    private EstrategiaDano estrategiaDano;

    public void setEstadoAtual(EstadoCombate novoEstado) {
        this.estadoAtual = novoEstado;
    }

    public void definirEstrategiaPorPosicao(String posicao) {
        if ("VANGUARDA".equalsIgnoreCase(posicao)) {
            this.estrategiaDano = new EstrategiaVanguarda();
        } else {
            this.estrategiaDano = new EstrategiaRetaguarda();
        }
    }

    public int executarAtaque(int atq, int arma, int def) {
        return estrategiaDano.calcular(atq, arma, def);
    }
}

public interface EstadoCombate {
    void processarTurno(ContextoBatalha contexto);
}

public class TurnoJogadorState implements EstadoCombate {
    @Override
    public void processarTurno(ContextoBatalha contexto) {
        contexto.setEstadoAtual(new TurnoInimigoState());
    }
}
```