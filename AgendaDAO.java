import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AgendaDAO {

    // INSERT: grava uma consulta no banco
    public void salvar(Consulta consulta) throws SQLException {
        Connection conexao = null;
        PreparedStatement ps = null;
        try {
            conexao = ConexaoBD.conectar();
            String sql = "INSERT INTO agenda (horario, tipo, nome, idade, nome_dono, "
                    + "telefone_dono, detalhe, sintomas) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            ps = conexao.prepareStatement(sql);
            Animal a = consulta.getPaciente();
            ps.setInt(1, consulta.getHorarioIndex());
            ps.setString(2, a.getEspecie());
            ps.setString(3, a.getNome());
            ps.setInt(4, a.getIdade());
            ps.setString(5, a.getNomeDono());
            ps.setString(6, a.getTelefoneDono());
            ps.setString(7, a.getDetalhe());
            ps.setString(8, consulta.getSintomas());
            ps.executeUpdate();
        } finally {
            fechar(conexao, ps, null);
        }
    }

    // SELECT: devolve um vetor com os 10 horários (posição sem consulta = null)
    public Consulta[] buscarAgenda() throws SQLException {
        Consulta[] agenda = new Consulta[10];
        Connection conexao = null;
        PreparedStatement ps = null;
        ResultSet resultado = null;
        try {
            conexao = ConexaoBD.conectar();
            ps = conexao.prepareStatement("SELECT * FROM agenda");
            resultado = ps.executeQuery();
            while (resultado.next()) {
                int horario = resultado.getInt("horario");
                String tipo = resultado.getString("tipo");
                String nome = resultado.getString("nome");
                int idade = resultado.getInt("idade");
                String nomeDono = resultado.getString("nome_dono");
                String telefoneDono = resultado.getString("telefone_dono");
                String detalhe = resultado.getString("detalhe");
                String sintomas = resultado.getString("sintomas");

                Animal paciente;
                if (tipo.equals("Cachorro")) {
                    paciente = new Cachorro(nome, idade, nomeDono, telefoneDono, detalhe);
                } else {
                    paciente = new Gato(nome, idade, nomeDono, telefoneDono, detalhe);
                }
                agenda[horario] = new Consulta(paciente, horario, sintomas);
            }
        } finally {
            fechar(conexao, ps, resultado);
        }
        return agenda;
    }

    // UPDATE: altera os dados da consulta que está em horarioAtual (pode mudar o próprio horário)
    public void alterar(int horarioAtual, Consulta nova) throws SQLException {
        Connection conexao = null;
        PreparedStatement ps = null;
        try {
            conexao = ConexaoBD.conectar();
            String sql = "UPDATE agenda SET horario = ?, tipo = ?, nome = ?, idade = ?, "
                    + "nome_dono = ?, telefone_dono = ?, detalhe = ?, sintomas = ? "
                    + "WHERE horario = ?";
            ps = conexao.prepareStatement(sql);
            Animal a = nova.getPaciente();
            ps.setInt(1, nova.getHorarioIndex());
            ps.setString(2, a.getEspecie());
            ps.setString(3, a.getNome());
            ps.setInt(4, a.getIdade());
            ps.setString(5, a.getNomeDono());
            ps.setString(6, a.getTelefoneDono());
            ps.setString(7, a.getDetalhe());
            ps.setString(8, nova.getSintomas());
            ps.setInt(9, horarioAtual);
            ps.executeUpdate();
        } finally {
            fechar(conexao, ps, null);
        }
    }

    // DELETE: cancela a consulta e libera o horário
    public void cancelar(int horario) throws SQLException {
        Connection conexao = null;
        PreparedStatement ps = null;
        try {
            conexao = ConexaoBD.conectar();
            ps = conexao.prepareStatement("DELETE FROM agenda WHERE horario = ?");
            ps.setInt(1, horario);
            ps.executeUpdate();
        } finally {
            fechar(conexao, ps, null);
        }
    }

    // Fechar recursos
    private void fechar(Connection conexao, PreparedStatement ps, ResultSet resultado) {
        try {
            if (resultado != null) {
                resultado.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conexao != null) {
                conexao.close();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao fechar recursos: " + e.getMessage());
        }
    }
}
