# 🧙‍♂️ Microsserviço: `personagens-service`
*(Responsável pela Implementação: ---)*

## 🎯 Objetivo do Serviço
Este microsserviço é responsável por gerenciar o ciclo de vida e a ficha técnica dos personagens dos jogadores. Ele controla a criação customizada de classes, persistência de atributos essenciais no banco de dados H2, computação de ganho de XP, progressão de nível (Level Up) e a evolução para os Arquétipos de Elite.

---

## 📝 Checklist de Implementação
- [ ] **Persistência Base:** Criar a entidade JPA `Personagem` mapeada com os 4 atributos fundamentais. A tabela deve conter uma coluna `usuarioId` (Tipo `Long`) para criar um vínculo lógico. *Nota de desenvolvimento: Como o auth-service está em implementação, você pode mockar/passar qualquer número de ID nos testes locais do H2 para criar o personagem.*
    * `POST /personagens` -> Para criar um novo herói.
    * `GET /personagens/usuario/{usuarioId}` -> Para carregar a ficha do jogador ativo.
      
- [ ] **Lógica de Progressão:** Implementar o método de ganho de XP e a trigger de `subirNivel()`.
- [ ] **Padrão GoF - Builder:** Criar o `PersonagemBuilder` para flexibilizar a atribuição de status numéricos na ficha sem expor construtores gigantescos.
- [ ] **Padrão GoF - Factory Method:** Criar a `PersonagemFactory` para encapsular a regra de negócio do RPG e injetar os status iniciais corretos de cada classe usando o Builder.

- Obs: Se houver mais endpoints que agregem no desenvolvimento pode adicionar
---

## 📊 Atributos e Balanceamento Base (Nível 1)

Para manter o escopo do motor de combate limpo e performático, o sistema utiliza apenas **4 atributos fundamentais** de sobrevivência, dano e posicionamento tático:

1. **Guerreiro** (Posição Base: `VANGUARDA`)
    * 🩸 Vida Máxima (HP): 120 | ⚔️ Ataque: 25 | 🛡️ Defesa: 15
2. **Mago** (Posição Base: `RETAGUARDA`)
    * 🩸 Vida Máxima (HP): 75 | ⚔️ Ataque: 40 | 🛡️ Defesa: 5
3. **Arqueiro** (Posição Base: `RETAGUARDA`)
    * 🩸 Vida Máxima (HP): 90 | ⚔️ Ataque: 30 | 🛡️ Defesa: 10
4. **Clérigo** (Posição Base: `VANGUARDA` ou `RETAGUARDA` - Híbrido)
    * 🩸 Vida Máxima (HP): 100 | ⚔️ Ataque: 20 | 🛡️ Defesa: 12

---

## 📈 Tabela de Evolução e Progressão Linear (Níveis 2 a 4)

A cada nível avançado antes do ápice, o personagem recebe um incremento fixo nos seus status básicos ao acumular XP suficiente:
* **Guerreiro:** +20 HP | +4 Ataque | +2 Defesa (por nível)
* **Mago:** +10 HP | +6 Ataque | +1 Defesa (por nível)
* **Arqueiro:** +15 HP | +5 Ataque | +1 Defesa (por nível)
* **Clérigo:** +15 HP | +3 Ataque | +2 Defesa (por nível)

---

## 👑 Arquétipos de Elite e Upgrades Consolidados (Níveis 5 e 10)

Ao atingir o **Nível 5**, o jogador escolhe obrigatoriamente um dos caminhos de especialização da sua árvore de classe. Essa escolha destrava bônus massivos, consolidando os status finais para o **Nível 5** e, posteriormente, para o limite lendário do **Nível 10**:

### ⚔️ Linha do Guerreiro
* **Paladino** (Foco: Defesa Absoluta e Sobrevivência na Vanguarda)
    * **Nível 5:** 250 HP | 40 Ataque | 30 Defesa
    * **Nível 10 (Lendário):** 450 HP | 65 Ataque | 55 Defesa
* **Berserker** (Foco: Dano Bruto e Críticos Massivos)
    * **Nível 5:** 200 HP | 55 Ataque | 20 Defesa
    * **Nível 10 (Lendário):** 360 HP | 95 Ataque | 35 Defesa

### 🔮 Linha do Mago
* **Feiticeiro** (Foco: Explosão de Dano Mágico Puro / Glass Cannon)
    * **Nível 5:** 115 HP | 85 Ataque | 8 Defesa
    * **Nível 10 (Lendário):** 195 HP | 155 Ataque | 15 Defesa
* **Mago de Combate / Battlemage** (Foco: Resistência Mágica e Combate Híbrido)
    * **Nível 5:** 140 HP | 65 Ataque | 18 Defesa
    * **Nível 10 (Lendário):** 260 HP | 115 Ataque | 35 Defesa

### 🏹 Linha do Arqueiro
* **Atirador de Elite / Sniper** (Foco: Tiros Fatais à Distância / Retaguarda)
    * **Nível 5:** 150 HP | 65 Ataque | 12 Defesa
    * **Nível 10 (Lendário):** 270 HP | 120 Ataque | 22 Defesa
* **Ranger / Caçador** (Foco: Versatilidade Tática e Agilidade Equilibrada)
    * **Nível 5:** 175 HP | 50 Ataque | 18 Defesa
    * **Nível 10 (Lendário):** 315 HP | 90 Ataque | 32 Defesa

### ☀️ Linha do Clérigo
* **Sacerdote** (Foco: Cura Máxima e Suporte na Retaguarda)
    * **Nível 5:** 160 HP | 30 Ataque | 20 Defesa
    * **Nível 10 (Lendário):** 290 HP | 55 Ataque | 38 Defesa
* **Inquisidor** (Foco: Ofensiva Sagrada e Combate de Vanguarda)
    * **Nível 5:** 190 HP | 45 Ataque | 24 Defesa
    * **Nível 10 (Lendário):** 330 HP | 85 Ataque | 44 Defesa

---

## 💡 Exemplo Estrutural dos Padrões (Factory + Builder)

*Guia de código para garantir que a inicialização do personagem respeite o acoplamento correto dos padrões GoF exigidos:*

```java
// Builder para montagem limpa do objeto
public class PersonagemBuilder {
    private String nome;
    private int vida;
    private int ataque;
    private int defesa;
    private String posicao;

    public PersonagemBuilder nome(String nome) { this.nome = nome; return this; }
    public PersonagemBuilder vida(int vida) { this.vida = vida; return this; }
    public PersonagemBuilder ataque(int ataque) { this.ataque = ataque; return this; }
    public PersonagemBuilder defesa(int defesa) { this.defesa = defesa; return this; }
    public PersonagemBuilder posicao(String posicao) { this.posicao = posicao; return this; }
    
    public Personagem build() {
        return new Personagem(nome, vida, ataque, defesa, posicao);
    }
}

// Factory encapsulando as regras táticas das classes nível 1
public class PersonagemFactory {
    public static Personagem criarPersonagemInicial(String nome, String classe) {
        switch (classe.toUpperCase()) {
            case "GUERREIRO":
                return new PersonagemBuilder().nome(nome).vida(120).ataque(25).defesa(15).posicao("VANGUARDA").build();
            case "MAGO":
                return new PersonagemBuilder().nome(nome).vida(75).ataque(40).defesa(5).posicao("RETAGUARDA").build();
            case "ARQUEIRO":
                return new PersonagemBuilder().nome(nome).vida(90).ataque(30).defesa(10).posicao("RETAGUARDA").build();
            case "CLERIGO":
                return new PersonagemBuilder().nome(nome).vida(100).ataque(20).defesa(12).posicao("VANGUARDA").build();
            default:
                throw new IllegalArgumentException("Classe base desconhecida para o RPG.");
        }
    }
}