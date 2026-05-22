# 🎒 Microsserviço: `inventario-service`
*(Responsável pela Implementação: ---)*

## 🎯 Objetivo do Serviço
Este microsserviço é o responsável por toda a camada de economia, armazenamento e customização de equipamentos dos jogadores. Ele gerencia os itens e consumíveis guardados na mochila de cada herói e, principalmente, implementa o sistema dinâmico de **Tags de Arma** (modificadores físicos e temporais), alterando o comportamento e o poder das armas em tempo de execução.

---

## 📝 Checklist de Implementação
- [ ] **Persistência de Dados:** Criar as entidades JPA para `Item` (id, nome, tipo, danoBase) e `Inventario` (vínculo lógico contendo `usuarioId`, `itemId` e quantidade).
- [ ] **Endpoints REST:**
    - `GET /inventario/usuario/{usuarioId}` -> Retorna a lista de itens da mochila do jogador.
    - `POST /inventario/equipar` -> Define qual arma está ativa para o combate.
- [ ] **Padrão GoF - Decorator:** Implementar a estrutura que envolve uma arma base com "Tags" (Pesada, Acuidade, Recarga ou Encantamentos) para adicionar bônus/penalidades dinamicamente.

- Obs: Se houver mais endpoints que agregem no desenvolvimento pode adicionar
---

## ⚔️ Mecânica das Tags de Arma (Decorator)

O sistema de inventário utiliza o padrão **Decorator** para evitar duplicação de armas no banco de dados e permitir modificações dinâmicas em tempo de execução.

Em vez de criar várias versões de uma mesma arma, ela é construída em memória com empilhamento de modificadores (tags).

### 🔧 Tipos de Tags

- **Pesada:** Aumenta dano bruto, mas reduz agilidade.
- **Acuidade:** Aumenta chance de crítico baseada em velocidade.
- **Recarga:** Impõe delays ou altera ritmo de ataques.
- **Encantamentos (futuro):** Efeitos elementais ou mágicos.

---

## 💡 Implementação do Padrão (Decorator)

```java
// Interface base
public interface ArmaComponent {
    String getDescricao();
    int getDanoTotal();
}

// Arma base vinda do banco
public class ArmaBase implements ArmaComponent {
    private String nome;
    private int danoBase;

    public ArmaBase(String nome, int danoBase) {
        this.nome = nome;
        this.danoBase = danoBase;
    }

    @Override
    public String getDescricao() {
        return nome;
    }

    @Override
    public int getDanoTotal() {
        return danoBase;
    }
}

// Decorator base
public abstract class WeaponTagDecorator implements ArmaComponent {
    protected ArmaComponent arma;

    public WeaponTagDecorator(ArmaComponent arma) {
        this.arma = arma;
    }
}

// Tag Pesada
public class TagPesada extends WeaponTagDecorator {

    public TagPesada(ArmaComponent arma) {
        super(arma);
    }

    @Override
    public String getDescricao() {
        return arma.getDescricao() + " [Pesada]";
    }

    @Override
    public int getDanoTotal() {
        return arma.getDanoTotal() + 15;
    }
}

// Tag Acuidade
public class TagAcuidade extends WeaponTagDecorator {

    public TagAcuidade(ArmaComponent arma) {
        super(arma);
    }

    @Override
    public String getDescricao() {
        return arma.getDescricao() + " [Acuidade]";
    }

    @Override
    public int getDanoTotal() {
        return arma.getDanoTotal() + 7;
    }
}
```

---

## ⚔️ Como isso é usado no Combate?

O `inventario-service` monta a arma dinamicamente antes de enviar para o `combate-service`.

Exemplo:

```java
ArmaComponent arma = new ArmaBase("Espada de Ferro", 20);
arma = new TagPesada(arma);
arma = new TagAcuidade(arma);

System.out.println(arma.getDanoTotal()); // dano final calculado em tempo real
```

---

## 🔗 Integração com o Combate

O `combate-service` consome o inventário via Feign Client e recebe:

- arma equipada já decorada
- dano final calculado
- ou estrutura base para reconstrução dos decorators

Isso permite que o combate apenas **aplique regras de batalha**, sem se preocupar com construção de item.

---

## 🧠 Nota de Arquitetura

- O banco guarda apenas **estado bruto do item**
- As Tags vivem em **tempo de execução**
- O padrão Decorator evita explosão de classes e duplicação de armas

---

💡 Resultado final: o inventário vira um sistema flexível, extensível e preparado para expansão de novos tipos de modificadores sem alterar o banco de dados.