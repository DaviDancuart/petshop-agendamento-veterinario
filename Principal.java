import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Principal {

    public static void main(String[] args) {
        AgendaDAO dao = new AgendaDAO();
        String[] botoes = {"1. Agendar Consulta", "2. Ver Agenda Diária",
            "3. Alterar Agendamento", "4. Cancelar Agendamento", "5. Sair"};
        int opcao;

        do {
            opcao = JOptionPane.showOptionDialog(null, "Escolha uma opção:",
                    "PetShop - Menu Principal",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    botoes,
                    botoes[0]);

            try {
                if (opcao == 0) {
                    agendar(dao);
                } else if (opcao == 1) {
                    verAgenda(dao);
                } else if (opcao == 2) {
                    alterar(dao);
                } else if (opcao == 3) {
                    cancelar(dao);
                }
            } catch (AgendaLotadaException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(),
                        "Agenda lotada", JOptionPane.WARNING_MESSAGE);
            } catch (HorarioOcupadoException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(),
                        "Horário ocupado", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,
                        "Erro de banco de dados: " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } while (opcao != 4 && opcao != -1);

        JOptionPane.showMessageDialog(null, "Sistema encerrado.");
    }

    // ---------- 1. Agendar Consulta ----------
    public static void agendar(AgendaDAO dao)
            throws SQLException, AgendaLotadaException, HorarioOcupadoException {

        Consulta[] agenda = dao.buscarAgenda();

        // Validação: agenda lotada
        boolean temVaga = false;
        for (Consulta c : agenda) {
            if (c == null) {
                temVaga = true;
            }
        }
        if (!temVaga) {
            throw new AgendaLotadaException();
        }

        String[] especies = {"Cachorro", "Gato"};
        int especie = JOptionPane.showOptionDialog(null, "Qual a espécie do animal?",
                "Agendar Consulta",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                especies,
                especies[0]);
        if (especie == -1) {
            return;
        }

        int horario = lerInteiro("Horário desejado (0 a 9):", 9);
        if (horario == -1) {
            return;
        }

        // Validação: conflito de horário
        if (agenda[horario] != null) {
            throw new HorarioOcupadoException();
        }

        String nomeDono = lerTexto("Nome do tutor:", 50);
        if (nomeDono == null) {
            return;
        }
        String telefone = lerTexto("Telefone do tutor:", 20);
        if (telefone == null) {
            return;
        }
        String nomeAnimal = lerTexto("Nome do animal:", 50);
        if (nomeAnimal == null) {
            return;
        }
        int idade = lerInteiro("Idade do animal (em anos):", 50);
        if (idade == -1) {
            return;
        }

        String detalhe;
        if (especie == 0) {
            detalhe = lerTexto("Porte do cachorro (pequeno, médio ou grande):", 30);
        } else {
            detalhe = lerTexto("Temperamento do gato (ex.: dócil, arisco):", 30);
        }
        if (detalhe == null) {
            return;
        }

        String sintomas = lerTexto("Sintomas / motivo da consulta:", 200);
        if (sintomas == null) {
            return;
        }

        // Polimorfismo de tipo: a variável é Animal, o objeto é Cachorro ou Gato
        Animal paciente;
        if (especie == 0) {
            paciente = new Cachorro(nomeAnimal, idade, nomeDono, telefone, detalhe);
        } else {
            paciente = new Gato(nomeAnimal, idade, nomeDono, telefone, detalhe);
        }

        Consulta consulta = new Consulta(paciente, horario, sintomas);
        dao.salvar(consulta);

        JOptionPane.showMessageDialog(null,
                "Consulta agendada para o horário " + horario + "!",
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    // ---------- 2. Ver Agenda Diária ----------
    public static void verAgenda(AgendaDAO dao) throws SQLException {
        Consulta[] agenda = dao.buscarAgenda();
        String texto = "";

        for (int i = 0; i < agenda.length; i++) {
            if (agenda[i] == null) {
                texto = texto + "Horário [" + i + "]: [Disponível]\n";
            } else {
                Animal a = agenda[i].getPaciente();
                texto = texto + "Horário [" + i + "]: " + a.getNome()
                        + " | " + a.getEspecie()
                        + " | Tutor: " + a.getNomeDono()
                        + " | Som: " + a.emitirSom() + "\n";   // polimorfismo de sobrescrita
            }
        }

        JOptionPane.showMessageDialog(null, texto,
                "Agenda Diária", JOptionPane.PLAIN_MESSAGE);
    }

    // ---------- 3. Alterar Agendamento ----------
    public static void alterar(AgendaDAO dao) throws SQLException, HorarioOcupadoException {
        Consulta[] agenda = dao.buscarAgenda();

        int horarioAtual = lerInteiro("Horário a alterar (0 a 9):", 9);
        if (horarioAtual == -1) {
            return;
        }

        // Validação: horário vazio
        if (agenda[horarioAtual] == null) {
            JOptionPane.showMessageDialog(null,
                    "O horário " + horarioAtual + " está vazio. Não há consulta para alterar.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Consulta consultaAtual = agenda[horarioAtual];
        Animal atual = consultaAtual.getPaciente();
        boolean cachorro = atual instanceof Cachorro;   // a espécie não muda

        // Cada campo abre já preenchido com o valor atual
        int horario = lerInteiro("Novo horário (0 a 9):", 9, String.valueOf(horarioAtual));
        if (horario == -1) {
            return;
        }

        // Validação: conflito de horário (só quando o horário mudou)
        if (horario != horarioAtual && agenda[horario] != null) {
            throw new HorarioOcupadoException();
        }

        String nomeDono = lerTexto("Nome do tutor:", atual.getNomeDono(), 50);
        if (nomeDono == null) {
            return;
        }
        String telefone = lerTexto("Telefone do tutor:", atual.getTelefoneDono(), 20);
        if (telefone == null) {
            return;
        }
        String nomeAnimal = lerTexto("Nome do animal:", atual.getNome(), 50);
        if (nomeAnimal == null) {
            return;
        }
        int idade = lerInteiro("Idade do animal (em anos):", 50, String.valueOf(atual.getIdade()));
        if (idade == -1) {
            return;
        }

        String detalhe;
        if (cachorro) {
            detalhe = lerTexto("Porte do cachorro (pequeno, médio ou grande):", atual.getDetalhe(), 30);
        } else {
            detalhe = lerTexto("Temperamento do gato (ex.: dócil, arisco):", atual.getDetalhe(), 30);
        }
        if (detalhe == null) {
            return;
        }

        String sintomas = lerTexto("Sintomas / motivo da consulta:", consultaAtual.getSintomas(), 200);
        if (sintomas == null) {
            return;
        }

        // Mesma espécie de antes: a variável é Animal, o objeto é Cachorro ou Gato
        Animal paciente;
        if (cachorro) {
            paciente = new Cachorro(nomeAnimal, idade, nomeDono, telefone, detalhe);
        } else {
            paciente = new Gato(nomeAnimal, idade, nomeDono, telefone, detalhe);
        }
        Consulta nova = new Consulta(paciente, horario, sintomas);

        // Resumo + confirmação antes de gravar
        String textoHorario;
        if (horario == horarioAtual) {
            textoHorario = horario + " (mantido)";
        } else {
            textoHorario = horarioAtual + " -> " + horario;
        }
        String resumo = "Confirmar a alteração da consulta?\n\n"
                + "Espécie: " + paciente.getEspecie() + "\n"
                + "Horário: " + textoHorario + "\n"
                + "Tutor: " + nomeDono + "\n"
                + "Telefone: " + telefone + "\n"
                + "Animal: " + nomeAnimal + "\n"
                + "Idade: " + idade + "\n"
                + (cachorro ? "Porte: " : "Temperamento: ") + detalhe + "\n"
                + "Sintomas: " + quebrarLinhas(sintomas, 60);

        int confirmar = JOptionPane.showConfirmDialog(null, resumo,
                "Confirmar alteração",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmar == JOptionPane.YES_OPTION) {
            dao.alterar(horarioAtual, nova);
            JOptionPane.showMessageDialog(null,
                    "Consulta alterada! Ela agora está no horário " + horario + ".",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ---------- 4. Cancelar Agendamento ----------
    public static void cancelar(AgendaDAO dao) throws SQLException {
        Consulta[] agenda = dao.buscarAgenda();

        int horario = lerInteiro("Horário a cancelar (0 a 9):", 9);
        if (horario == -1) {
            return;
        }

        // Validação: horário já vazio
        if (agenda[horario] == null) {
            JOptionPane.showMessageDialog(null,
                    "O horário " + horario + " já está vazio.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(null,
                "Cancelar a consulta de " + agenda[horario].getPaciente().getNome() + "?",
                "Confirmar cancelamento",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmar == JOptionPane.YES_OPTION) {
            dao.cancelar(horario);
            JOptionPane.showMessageDialog(null,
                    "Consulta cancelada. O horário " + horario + " está livre.",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ---------- Métodos auxiliares de entrada ----------

    // Lê um texto não vazio, com no máximo "max" caracteres (tamanho da coluna no banco).
    // Retorna null se o usuário cancelar ou fechar a janela.
    public static String lerTexto(String mensagem, int max) {
        return lerTexto(mensagem, null, max);
    }

    // Igual ao anterior, mas o campo já abre preenchido com valorInicial (usado no Alterar).
    public static String lerTexto(String mensagem, String valorInicial, int max) {
        String texto;
        boolean ok = false;
        do {
            texto = (String) JOptionPane.showInputDialog(null, mensagem, "PetShop",
                    JOptionPane.QUESTION_MESSAGE, null, null, valorInicial);
            if (texto == null) {
                return null;
            }
            texto = texto.trim();
            if (texto.length() == 0) {
                JOptionPane.showMessageDialog(null, "Preencha o campo.",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
            } else if (texto.length() > max) {
                // nome do campo = início da mensagem, antes do ":" ou do "("
                JOptionPane.showMessageDialog(null, "O campo " + mensagem.split("[(:]")[0].trim()
                        + " aceita no máximo " + max + " caracteres.",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
            } else {
                ok = true;
            }
        } while (!ok);
        return texto;
    }

    // Lê um inteiro entre 0 e max usando Integer.parseInt.
    // Retorna -1 se o usuário cancelar ou fechar a janela.
    public static int lerInteiro(String mensagem, int max) {
        return lerInteiro(mensagem, max, null);
    }

    // Igual ao anterior, mas o campo já abre preenchido com valorInicial (usado no Alterar).
    public static int lerInteiro(String mensagem, int max, String valorInicial) {
        int valor = 0;
        boolean ok = false;
        do {
            String texto = (String) JOptionPane.showInputDialog(null, mensagem, "PetShop",
                    JOptionPane.QUESTION_MESSAGE, null, null, valorInicial);
            if (texto == null) {
                return -1;
            }
            try {
                valor = Integer.parseInt(texto.trim());
                if (valor >= 0 && valor <= max) {
                    ok = true;
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Digite um número entre 0 e " + max + ".",
                            "Valor inválido", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Digite apenas números inteiros.",
                        "Valor inválido", JOptionPane.ERROR_MESSAGE);
            }
        } while (!ok);
        return valor;
    }

    // Quebra um texto longo em linhas de até "largura" caracteres, para que a janela
    // de confirmação do Alterar não fique larga demais (sintomas podem ter 200 caracteres).
    public static String quebrarLinhas(String texto, int largura) {
        StringBuilder resultado = new StringBuilder();
        String linha = "";
        for (String palavra : texto.split(" ")) {
            while (palavra.length() > largura) {   // palavra maior que a linha: corta
                if (linha.length() > 0) {
                    resultado.append(linha).append("\n");
                    linha = "";
                }
                resultado.append(palavra, 0, largura).append("\n");
                palavra = palavra.substring(largura);
            }
            if (linha.length() > 0 && linha.length() + 1 + palavra.length() > largura) {
                resultado.append(linha).append("\n");
                linha = palavra;
            } else if (linha.length() == 0) {
                linha = palavra;
            } else {
                linha = linha + " " + palavra;
            }
        }
        return resultado.append(linha).toString();
    }
}
