import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conectaDAO {

    Connection conn;
    public String url = "jdbc:mysql://localhost:3306/leiloestdsat_bd?useSSL=false&allowPublicKeyRetrieval=true";
    public String user = "root";
    public String password = "root";

    // Método que abre a conexão e já retorna o objeto Connection
    public Connection conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Conexão realizada com sucesso!");
            return conn; // Retorna a conexão aberta
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Falha na conexão: " + ex.getMessage());
            return null;
        }
    }

    public Connection getConexao() {
        return this.conn;
    }

    public void desconectar() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao desconectar: " + ex.getMessage());
        }
    }
}

