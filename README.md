# 🏛️ Padrões e Desenho de Software — Portfólio de Engenharia de Software

<div align="center">

![Java](https://img.shields.io/badge/Java-17%2B%20%7C%2021-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Paradigm](https://img.shields.io/badge/Paradigm-Design%20Patterns%20%26%20OOP-4CAF50?style=for-the-badge)
![Refactoring](https://img.shields.io/badge/Code%20Quality-Clean%20Code%20%26%20SOLID-orange?style=for-the-badge)
![Academic](https://img.shields.io/badge/Curricular%20Unit-PDS%202024%2F2025-blue?style=for-the-badge)
![Institution](https://img.shields.io/badge/Institution-EST%20%2F%20IPCB-red?style=for-the-badge)
![License](https://img.shields.io/badge/License-Academic-lightgrey?style=for-the-badge)

<p align="center">
  <b>Repositório monorepo com os projetos práticos desenvolvidos na unidade curricular de Padrões e Desenho de Software da Licenciatura em Engenharia Informática (EST/IPCB). Demonstração prática dos padrões GoF (Gang of Four), princípios SOLID, refactoring de código legado e arquitetura orientada a objetos em Java.</b>
</p>

[Contexto Académico](#-contexto-académico) •
[Projetos do Portfólio](#-projetos-do-portfólio) •
[Matriz de Padrões de Desenho](#-matriz-global-de-padrões-de-desenho) •
[Compilação e Execução](#-compilação-e-execução-global) •
[Autores](#-autores)

</div>

---

## 📌 Contexto Académico

* **Unidade Curricular:** Padrões e Desenho de Software (PDS)
* **Grau:** Licenciatura em Engenharia Informática (LEI) — 3.º Ano / 1.º Semestre (2024/2025)
* **Instituição:** Escola Superior de Tecnologia (EST) — Instituto Politécnico de Castelo Branco (IPCB)
* **Docente Responsável:** Prof. Doutor Rogério Eduardo da Silva
* **Autores:**
  * **Miguel Fortunato Custóias** ([GitHub](https://github.com/MiguelCustoias))
  * **Rafael Lourenço Cruz** ([GitHub](https://github.com/RafaCr3z) • [LinkedIn](https://linkedin.com/in/rafael-cruz-7159092b2))

---

## 🚀 Projetos do Portfólio

| Projeto | Domínio | Foco Arquitetural & Competências | Link |
| :--- | :--- | :--- | :--- |
| **TP1: EST Global Airways** | Sistema de Gestão & Reservas de Voos | Modelação OOP pura, Facade, Factory Method, herança polimórfica, parsing de ficheiros planos, GUI Java Swing multi-janela. | [Ver Projeto ↗](./TP1_ESTGlobalAirways) |
| **TP2: Banco do FaroEST** | Refactoring & Expansões de Jogo Arcade | State Pattern (hierarquia e grafo dinâmico), Prototype, Abstract Factory, Strategy, Template Method, Singleton, eliminação de *Code Smells* (`switch` e `instanceof`). | [Ver Projeto ↗](./TP2_BancoFaroEST) |

---

### 1. [TP1 — EST Global Airways](./TP1_ESTGlobalAirways)
Solução de software completa para gestão de tráfego aéreo regional e internacional centrada no aeroporto de Castelo Branco.
* **Leitura & Processamento de Ficheiros:** Carregamento estruturado de aeroportos e voos (`.dat`).
* **Motor de Pesquisa e Filtros:** Pesquisa avançada por origem, destino, data de partida e lotação residual em cabine.
* **Ciclo de Vida de Reservas:** Emissão de localizadores alfanuméricos com 6 carateres, alteração de datas/passageiros/lugares, cálculo tarifário por 3 classes de conforto e 4 planos de reserva, taxação de bagagens de porão e cancelamento com reembolso.

### 2. [TP2 — Banco do FaroEST (Expansion Packs)](./TP2_BancoFaroEST)
Intervenção profunda de reengenharia e extensão sobre uma base de código legado de um jogo arcade em tempo real (segurança de banco controlando 3 portas simultâneas).
* **Eliminação de Code Smells:** Remoção sistemática de cadeias `switch/case` e verificações frágeis de tipo com `instanceof`.
* **Expansion Packs Dinâmicos:** Adição desacoplada dos packs *Zombies* (clientes abatidos reanimam com temporizador de ataque) e *Todos Roubam* (clientes tentam furtar os depósitos efetuados se não forem repelidos).
* **Inspeção de Estados em Grafo:** Integração da biblioteca GraphStream para visualização em tempo real das transições de estado dos clientes sem quebrar o princípio Open/Closed (OCP).

---

## 🧩 Matriz Global de Padrões de Desenho (GoF)

```
┌───────────────────────────┬─────────────────────────────────────┬───────────────────────────┐
│ Padrão de Desenho         │ TP1: EST Global Airways             │ TP2: Banco do FaroEST     │
├───────────────────────────┼─────────────────────────────────────┼───────────────────────────┤
│ Facade                    │ ESTAirways                          │ —                         │
│ Factory Method            │ ESTAirways.criarReserva(...)        │ LevelReader / Fabrica     │
│ Abstract Factory          │ —                                   │ VersaoFabrica (Packs)     │
│ State                     │ —                                   │ StatusCliente & PortaState│
│ Prototype                 │ —                                   │ Cliente (Cloneable)       │
│ Strategy                  │ —                                   │ GameControlStrategy       │
│ Template Method           │ —                                   │ LevelTemplate             │
│ Singleton                 │ —                                   │ ReguladorVelocidade       │
│ Observer / Event-Driven   │ Swing Listeners                     │ GameObserver              │
│ Polymorphism / OCP        │ Hierarquia Reserva                  │ StatusRenderer            │
└───────────────────────────┴─────────────────────────────────────┴───────────────────────────┘
```

---

## 📁 Estrutura do Repositório

```plaintext
pds-software-design-patterns/
├── docs/                                      # Ativos globais de documentação e diagramas
│   └── diagrams/                              # Diagramas UML e padrões GoF (PNG/JPEG)
├── TP1_ESTGlobalAirways/                      # Projeto 1: Sistema de Gestão e Reserva de Voos
│   ├── data/                                  # Datasets aeroportos.dat e voos.dat
│   ├── relatorio/                             # Relatório técnico original
│   ├── src/                                   # Código-fonte Java (estairways e menu)
│   └── README.md                              # Documentação detalhada do TP1
├── TP2_BancoFaroEST/                          # Projeto 2: Expansões de Jogo e Refactoring
│   ├── art/                                   # Sprites e recursos gráficos do jogo
│   ├── config/                                # Ficheiros de configuração de propriedades
│   ├── font/                                  # Tipografias para a interface
│   ├── lib/                                   # Bibliotecas externas GraphStream 2.0
│   ├── niveis/                                # Ficheiros descritores de fases
│   ├── relatorio/                             # Relatório técnico original
│   ├── src/                                   # Código-fonte Java (faroest e prof.jogos2D)
│   └── README.md                              # Documentação detalhada do TP2
├── .gitignore                                 # Regras universais de exclusão Java/IDEs
└── README.md                                  # Documentação principal (Root)
```

---

## 🛠️ Compilação e Execução Global

### Requisitos de Sistema
* **Java Development Kit (JDK):** Versão 17 LTS ou 21 LTS instalada.
* **Ambiente de Desenvolvimento:** Eclipse IDE for Java Developers, IntelliJ IDEA ou VS Code.
* **Sistema Operativo:** Windows, macOS ou Linux.

### Clonar o Repositório
```bash
git clone https://github.com/RafaCr3z/pds-software-design-patterns.git
cd pds-software-design-patterns
```

### Execução via IDE
1. Importe a pasta raiz ou cada uma das subpastas (`TP1_ESTGlobalAirways` e `TP2_BancoFaroEST`) como projetos Java no seu IDE.
2. Certifique-se de que o JDK 17+ está selecionado nas propriedades do projeto.
3. Para o **TP2**, garanta que os ficheiros `.jar` presentes em `TP2_BancoFaroEST/lib/` (ou `TP2_BancoFaroEST/src/`) se encontram adicionados ao *Build Path*.
4. Execute os respetivos pontos de entrada:
   * **TP1:** `TP1_ESTGlobalAirways/src/menu/Main.java`
   * **TP2:** `TP2_BancoFaroEST/src/faroest/app/BancoFaroEst.java`

---

## 👥 Autores

Trabalhos práticos desenvolvidos no âmbito da unidade curricular de **Padrões e Desenho de Software (2024/2025)**:

* **Miguel Fortunato Custódio** — [GitHub](https://github.com/MiguelCustoias)
* **Rafael Lourenço Cruz** — [GitHub](https://github.com/RafaCr3z) • [LinkedIn](https://linkedin.com/in/rafael-cruz-7159092b2)

---

<div align="center">
  <sub>Escola Superior de Tecnologia de Castelo Branco • Instituto Politécnico de Castelo Branco</sub><br>
  <sub>Licenciatura em Engenharia Informática — PDS (2024/2025)</sub>
</div>
