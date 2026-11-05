import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Principal {

    public static void main(String[] args) {
        AgendaDAO dao = new AgendaDAO();
        String[] botoes = {"1. Agendar Consulta", "2. Ver Agenda Diária",
            "3. Cancelar Agendamento", "4. Sair"};
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
        } while (opcao != 3 && opcao != -1);

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

        String nomeDono = lerTexto("Nome do tutor:");
        if (nomeDono == null) {
            return;
        }
        String telefone = lerTexto("Telefone do tutor:");
        if (telefone == null) {
            return;
        }
        String nomeAnimal = lerTexto("Nome do animal:");
        if (nomeAnimal == null) {
            return;
        }
        int idade = lerInteiro("Idade do animal (em anos):", 50);
        if (idade == -1) {
            return;
        }

        String detalhe;
        if (especie == 0) {
            detalhe = lerTexto("Porte do cachorro (pequeno, médio ou grande):");
        } else {
            detalhe = lerTexto("Temperamento do gato (ex.: dócil, arisco):");
        }
        if (detalhe == null) {
            return;
        }

        String sintomas = lerTexto("Sintomas / motivo da consulta:");
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

    // ---------- 3. Cancelar Agendamento ----------
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

    // Lê um texto não vazio. Retorna null se o usuário cancelar ou fechar a janela.
    public static String lerTexto(String mensagem) {
        String texto;
        boolean ok = false;
        do {
            texto = JOptionPane.showInputDialog(null, mensagem, "PetShop",
                    JOptionPane.QUESTION_MESSAGE);
            if (texto == null) {
                return null;
            }
            texto = texto.trim();
            if (texto.length() == 0) {
                JOptionPane.showMessageDialog(null, "Preencha o campo.",
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
        int valor = 0;
        boolean ok = false;
        do {
            String texto = JOptionPane.showInputDialog(null, mensagem, "PetShop",
                    JOptionPane.QUESTION_MESSAGE);
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
}
