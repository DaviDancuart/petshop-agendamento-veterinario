# PetShop – Sistema de Agendamento Veterinário

- **Disciplina:** Programação Orientada a Objetos II – 3º ano de Informática
- **Professora:** Adriana Nakahara
- **Integrantes:** Matheus Todeschini, Davi Dancuart, Luís

## O que o sistema faz

Sistema para a recepção de uma clínica veterinária organizar a agenda do dia, com menus e formulários em `JOptionPane` e os dados gravados em um banco MySQL (JDBC).

Menu principal:

1. **Agendar Consulta** – escolhe a espécie (Cachorro ou Gato) e o horário e informa tutor, telefone, nome e idade do animal, porte (cachorro) ou temperamento (gato) e sintomas.
2. **Ver Agenda Diária** – lista os 10 horários: os livres aparecem como `[Disponível]` e os ocupados mostram animal, espécie, tutor e o som (`Au Au!` ou `Miau!`).
3. **Alterar Agendamento** – muda os dados e/ou o horário de uma consulta (a espécie não muda). Os campos abrem preenchidos e há uma confirmação antes de gravar.
4. **Cancelar Agendamento** – libera o horário, com confirmação.
5. **Sair** – encerra o programa (fechar a janela do menu também encerra).

Regras:

- A clínica tem **10 horários por dia (0 a 9)**, com uma consulta por horário.
- Horário já ocupado: mensagem de erro. Agenda com os 10 horários ocupados: aviso de agenda lotada. Cancelar ou alterar um horário vazio: aviso.
- Os textos não podem ficar vazios e têm limite de caracteres: nomes 50, telefone 20, porte/temperamento 30 e sintomas 200. A idade é um número inteiro de 0 a 50.

## Classes

| Classe | Papel |
|---|---|
| `Animal` | Classe abstrata com nome, idade, tutor e telefone; método abstrato `emitirSom()`. |
| `Cachorro` | Subclasse de `Animal` com o atributo `porte`; o som é "Au Au!". |
| `Gato` | Subclasse de `Animal` com o atributo `temperamento`; o som é "Miau!". |
| `Consulta` | Associa um `Animal paciente` (Cachorro ou Gato) a um horário e aos sintomas. |
| `ConexaoBD` | Abre a conexão JDBC com o MySQL. |
| `AgendaDAO` | Acesso ao banco com `PreparedStatement`: `salvar`, `buscarAgenda`, `alterar` e `cancelar`. |
| `Principal` | Menu, formulários `JOptionPane` e validações. |
| `AgendaLotadaException` | Exceção para agenda com os 10 horários ocupados. |
| `HorarioOcupadoException` | Exceção para horário já ocupado. |
| `banco.sql` | Cria o banco `petshop` e a tabela `agenda`. |

## Requisitos

- JDK 8 ou superior
- MySQL 8.x
- MySQL Connector/J 8.4.0 (já incluído em `lib/`)

## Como preparar o banco

Com o MySQL em execução, na pasta do projeto:

1. Rode o `banco.sql` (no MySQL Workbench ou pelo terminal):

   ```
   mysql -u root -p -e "source banco.sql"
   ```

2. Como `root`, crie o usuário usado pelo programa, com permissão somente no banco `petshop`:

   ```sql
   CREATE USER IF NOT EXISTS 'petshop'@'localhost' IDENTIFIED BY '1234';
   GRANT ALL PRIVILEGES ON petshop.* TO 'petshop'@'localhost';
   ```

O programa conecta em `jdbc:mysql://localhost:3306/petshop` com o usuário `petshop` e a senha `1234` (veja `ConexaoBD.java`). Essa senha é só para uso local, nos estudos.

## Como executar

**Pelo VS Code**

1. Instale o *Extension Pack for Java*.
2. Abra a pasta do projeto (os `.jar` de `lib/` são reconhecidos automaticamente).
3. Abra o `Principal.java` e clique em **▶ Run** acima do `main`.

**Pelo terminal (Windows)**, na pasta do projeto:

```
javac -encoding UTF-8 -cp "lib/*" -d bin *.java
java -cp "bin;lib/*" Principal
```
