import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {

    // Dados de conexão com o banco (ajuste usuário e senha para o seu MySQL)
    private static String url = "jdbc:mysql://localhost:3306/petshop";
    private static String usuario = "petshop";
    private static String senha = "1234";

    public static Connection conectar() throws SQLException {
        // Carregar o driver do MySQL
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Erro ao carregar o driver: " + e.getMessage());
        }
        // Conectar ao banco de dados
        return DriverManager.getConnection(url, usuario, senha);
    }
}
