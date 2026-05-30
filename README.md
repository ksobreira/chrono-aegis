# ☽ Chrono Aegis: Sistema de Batalha RPG por Turnos

> **Disciplina:** T200-10 Projetos de Arquitetura de Sistemas
> **Projeto:** Game Architecture Challenge: Entrega 2

## 📖 Sobre o Projeto

O **Chrono Aegis** é um sistema de batalha RPG por turnos desenvolvido com arquitetura de microserviços em Spring Boot + Java no backend e React + Vite no frontend. O jogador cria personagens de diferentes classes, equipa armas no inventário e enfrenta monstros em batalhas por turnos com sistema de estados (veneno, atordoamento).

---

## 🏗️ Arquitetura

```
Frontend (React/Vite :5173)
          ↓
API Gateway (Spring Cloud Gateway :8080)
   ├── /auth/**          → auth-service        (:8081)
   ├── /personagens/**   → personagens-service (:8083)
   ├── /inventario/**    → inventario-service  (:8084)
   ├── /itens/**         → inventario-service  (:8084)
   └── /batalha/**       → combate-service     (:8082)

Eureka Server (:8761) ← todos os serviços registram aqui
```

### Microserviços

| Serviço | Porta | Responsabilidade |
|---------|-------|-----------------|
| `eureka-server` | 8761 | Service discovery |
| `api-gateway` | 8080 | Roteamento e CORS |
| `auth-service` | 8081 | Cadastro e autenticação de usuários |
| `personagens-service` | 8083 | Criação, evolução e gerenciamento de personagens |
| `inventario-service` | 8084 | Armas, itens e inventário dos personagens |
| `combate-service` | 8082 | Lógica de batalha por turnos |

---

## 🎮 Funcionalidades

- **Cadastro e login** de jogador
- **4 classes de personagem:** Guerreiro, Mago, Arqueiro, Clérigo
- **8 arquétipos de evolução** (disponíveis a partir do nível 4): Paladino, Berserker, Feiticeiro, Battlemage, Sniper, Ranger, Sacerdote, Inquisidor
- **Sistema de XP e níveis**  personagens evoluem ao vencer batalhas
- **Inventário com armas**  tags NORMAL, PESADA, ACUIDADE, RECARGA afetam o estilo de combate
- **Batalha por turnos** contra Goblin (nv.1-2), Ogro (nv.3-4) e Dragão (nv.5+)
- **Sistema de estados**  personagem pode ser envenenado (☠️) ou atordoado (💫) durante o combate
- **Front-end estilo pixel/Pokémon** com animações de batalha, barras de HP e sprites

---

## 🧩 Padrões de Projeto Implementados

### Criacionais (3)

| Padrão | Onde | Descrição |
|--------|------|-----------|
| **Factory Method** | `personagens-service` | `GuerreiroFactory`, `MagoFactory`, `ArqueiroFactory`, `ClerigoFactory` criam personagens com atributos específicos da classe |
| **Builder** | `personagens-service` | `PersonagemBuilder` constrói o objeto personagem passo a passo |
| **Factory** | `combate-service` | `MonstroFactory` gera o monstro adequado com base no nível do personagem |

### Estruturais (3)

| Padrão | Onde | Descrição |
|--------|------|-----------|
| **Decorator** | `inventario-service` | `FinesseWeaponDecorator`, `HeavyWeaponDecorator`, `LoadingWeaponDecorator` adicionam comportamentos às armas sem alterar a classe base |
| **Facade** | `combate-service` | `BatalhaFacade` simplifica a inicialização de uma sessão de batalha, orquestrando chamadas aos Feign clients e à factory |
| **Proxy (Feign Client)** | `combate-service` / `inventario-service` | `PersonagemClient` e `InventarioClient` funcionam como proxies para chamadas HTTP entre microserviços via Spring Cloud OpenFeign |

### Comportamentais (3)

| Padrão | Onde | Descrição |
|--------|------|-----------|
| **State** | `combate-service` | `TurnoJogadorState`, `TurnoInimigoState`, `ProcessandoEfeitosState`, `FimDeCombateState` — o fluxo da batalha muda de estado a cada fase. `EstadoVivo`, `EstadoAtordoado`, `EstadoEnvenenado`, `EstadoMorto` controlam o estado do personagem |
| **Strategy** | `combate-service` | `NormalDamageStrategy` e `FinesseStrategy` calculam o dano de formas diferentes dependendo da tag da arma e posição no combate |
| **Observer** | `combate-service` | `LogObserver` e `XPObserver` são notificados ao fim da batalha — o XPObserver chama o personagens-service via Feign para creditar XP |
| **Command** | `combate-service` | `AtacarCommand`, `FugirCommand`, `UsarItemCommand` encapsulam as ações do jogador |

> O projeto implementa **10 padrões** (acima do mínimo de 9 exigido).

---

## 🔍 POA:  Programação Orientada a Aspectos (Não obrigatório)

| Aspecto | Serviço | Função |
|---------|---------|--------|
| `AuditoriaAspect` | `auth-service` | Registra em log cada registro de usuário e login realizado com timestamp |
| `LogginAspect` | `combate-service` | Intercepta chamadas ao controller de batalha para logging automático das requisições |

Ambos implementados com **Spring AOP + AspectJ** usando `@Aspect`, `@AfterReturning` e `@Around`.

---

## ⚙️ Princípios SOLID e GRASP aplicados

| Princípio | Onde |
|-----------|------|
| **SRP** (Responsabilidade Única) | Cada microserviço tem uma única responsabilidade. Dentro do combate-service, cada classe de estado, strategy e command tem uma única função |
| **OCP** (Aberto/Fechado) | Novos tipos de armas se adicionam com novos Decorators sem alterar o código existente. Novas ações de batalha se adicionam com novos Commands |
| **LSP** (Substituição de Liskov) | As subclasses de `Personagem` (Guerreiro, Mago etc.) são intercambiáveis. As implementações de `EstadoPersonagem` e `DamageCalculationStrategy` são substituíveis |
| **ISP** (Segregação de Interface) | `EstadoCombate`, `EstadoPersonagem`, `DamageCalculationStrategy`, `BatalhaObserver` são interfaces específicas e coesas |
| **DIP** (Inversão de Dependência) | Controllers dependem de Services (interfaces/abstrações), não de implementações. Feign clients são injetados por interface |
| **Alta Coesão / Baixo Acoplamento** (GRASP) | Cada serviço é independente e se comunica apenas via HTTP através do gateway. Dentro do combate-service, cada classe tem responsabilidade única e clara |
| **Controlador** (GRASP) | Controllers recebem as requisições e delegam para a camada de Service/Facade |
| **Polimorfismo** (GRASP) | State, Strategy e Factory Method todos exploram polimorfismo para variar o comportamento em tempo de execução |

---

## 🗂️ Estrutura de Pastas

```
chrono-aegis/
├── backend/
│   ├── eureka-server/
│   ├── api-gateway/
│   │   └── src/.../config/CorsConfig.java
│   ├── auth-service/
│   │   └── src/.../aspect/AuditoriaAspect.java
│   ├── personagens-service/
│   │   └── src/.../
│   │       ├── builder/PersonagemBuilder.java
│   │       ├── factory/[Guerreiro|Mago|Arqueiro|Clerigo]Factory.java
│   │       ├── model/[Guerreiro|Mago|Arqueiro|Clerigo|Personagem].java
│   │       └── enums/[ClassePersonagem|Arquetipo|CombatPosition].java
│   ├── inventario-service/
│   │   └── src/.../
│   │       ├── decorator/[Base|Finesse|Heavy|Loading]Weapon*.java
│   │       ├── model/[Arma|Item|Inventario].java
│   │       └── client/PersonagemClient.java
│   └── combate-service/
│       └── src/.../
│           ├── aspect/LogginAspect.java
│           ├── command/[Atacar|Fugir|UsarItem]Command.java
│           ├── facade/BatalhaFacade.java
│           ├── factory/MonstroFactory.java
│           ├── observer/[Log|XP]Observer.java
│           ├── state/[TurnoJogador|TurnoInimigo|ProcessandoEfeitos|FimDeCombate]State.java
│           ├── state/[EstadoVivo|EstadoMorto|EstadoAtordoado|EstadoEnvenenado].java
│           └── strategy/[Normal|Finesse]DamageStrategy.java
├── frontend/
│   └── src/
│       ├── pages/[Login|Cadastro|Personagens|Inventario|Batalha].[jsx|css]
│       ├── components/[Navbar|PrivateRoute].[jsx|css]
│       └── services/[auth|personagem|inventario|combate]Service.js
```

---

## 🚀 Como Executar

### Pré-requisitos

- Java 21
- Maven instalado e no PATH
- Node.js 18+


O script inicia os serviços **na ordem correta** com delays para o Eureka subir primeiro. Acompanhe pelo dashboard em `http://localhost:8761`.

### Backend — iniciar manualmente (em ordem)

```bash
# 1. Eureka (aguarde "Started EurekaServerApplication")
cd eureka-server && mvn spring-boot:run

# 2. Serviços (podem subir em paralelo)
cd auth-service        && mvn spring-boot:run
cd personagens-service && mvn spring-boot:run
cd inventario-service  && mvn spring-boot:run
cd combate-service     && mvn spring-boot:run

# 3. Gateway (após todos estarem no Eureka)
cd api-gateway && mvn spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm run dev
# Acesse: http://localhost:5173
```

---

## 🌐 Todas as Rotas da API

Base URL: `http://localhost:8080`

### Auth — `/auth`
| Método | Rota | Body | Retorno |
|--------|------|------|---------|
| POST | `/auth/registrar` | `{ nomeJogador, email, senha }` | `UsuarioDTO` |
| POST | `/auth/login` | `{ email, senha }` | `UsuarioDTO` |
| GET | `/auth/usuarios` | — | `List<UsuarioDTO>` |
| GET | `/auth/usuarios/{id}` | — | `UsuarioDTO` |
| PUT | `/auth/usuarios/{id}` | `{ nomeJogador, email }` | `UsuarioDTO` |
| DELETE | `/auth/usuarios/{id}` | — | `String` |

### Personagens — `/personagens`
| Método | Rota | Body / Params | Retorno |
|--------|------|---------------|---------|
| POST | `/personagens` | `{ usuarioId, nome, classe }` | `Personagem` |
| GET | `/personagens/usuario/{usuarioId}` | — | `List<Personagem>` |
| GET | `/personagens/interno/{id}` | — | `Map` *(uso interno Feign)* |
| POST | `/personagens/{id}/ganhar-xp` | `?xp=50` | `Personagem` |
| POST | `/personagens/{id}/evoluir` | `?arquetipo=PALADINO` | `Personagem` |

**Classes:** `GUERREIRO` `MAGO` `ARQUEIRO` `CLERIGO`

**Arquétipos:** `PALADINO` `BERSERKER` `FEITICEIRO` `BATTLEMAGE` `SNIPER` `RANGER` `SACERDOTE` `INQUISIDOR`

### Inventário — `/inventario` e `/itens`
| Método | Rota | Body | Retorno |
|--------|------|------|---------|
| GET | `/inventario/{personagemId}` | — | `Inventario` |
| POST | `/inventario/equipar` | `{ personagemId, armaId }` | `Inventario` |
| POST | `/itens/armas` | `{ nome, danoBase, forcaMinima, tags }` | `Arma` |
| GET | `/itens/armas` | — | `List<Arma>` |
| GET | `/itens/armas/{id}` | — | `Arma` |
| POST | `/itens` | `{ nome, descricao, tipo, personagemId }` | `Item` |
| GET | `/itens/personagem/{personagemId}` | — | `List<Item>` |
| DELETE | `/itens/{id}` | — | `String` |

**Tags de arma:** `NORMAL` `PESADA` `ACUIDADE` `RECARGA`

### Combate — `/batalha`
| Método | Rota | Body | Retorno |
|--------|------|------|---------|
| POST | `/batalha/iniciar` | `{ personagemId }` | `BatalhaResponseDTO` |
| POST | `/batalha/acao` | `{ sessionId, tipoAcao }` | `ResultadoTurnoDTO` |

**Ações:** `ATACAR` `USAR_ITEM` `FUGIR`



## 🛠️ Tecnologias

### Backend
- Java 21
- Spring Boot 3.3.5
- Spring Cloud 2023.0.3 (Gateway, Eureka, OpenFeign)
- Spring Security
- Spring Data JPA + H2
- Spring AOP + AspectJ
- Lombok

### Frontend
- React 19 + Vite
- React Router DOM v7
- Axios
- CSS Modules (fonte: Press Start 2P — estilo pixel)

---