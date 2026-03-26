
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;


public class ProdutosDAO {
    
    Connection conn;
    PreparedStatement prep;
    ResultSet resultset;
    ArrayList<ProdutosDTO> listagem = new ArrayList<>();
    
    public void cadastrarProduto (ProdutosDTO produto){
     conectaDAO conecta = new conectaDAO();
    java.sql.Connection conn = conecta.conectar();
    
    String sql = "INSERT INTO produtos (nome, valor, status) VALUES (?, ?, ?)";
    
    try {
        java.sql.PreparedStatement prep = conn.prepareStatement(sql);
        prep.setString(1, produto.getNome());
        prep.setDouble(2, produto.getValor());
        prep.setString(3, produto.getStatus());
        
        prep.executeUpdate();
        conecta.desconectar();
        
    } catch (java.sql.SQLException e) {
        // Se der erro no banco, esta mensagem aparecerá
        javax.swing.JOptionPane.showMessageDialog(null, "Erro ao cadastrar no banco: " + e.getMessage());
    } 
    }
    
    public ArrayList<ProdutosDTO> listarProdutos(){
        
        return listagem;
    }
    
    public boolean venderProduto(int id) {
    conectaDAO conecta = new conectaDAO();
    Connection conn = conecta.conectar();
    boolean sucesso = false;

    try {
        // 1. Verifica status
        String sqlCheck = "SELECT status FROM produtos WHERE id = ?";
        PreparedStatement prepCheck = conn.prepareStatement(sqlCheck);
        prepCheck.setInt(1, id);
        ResultSet rs = prepCheck.executeQuery();

        if (rs.next()) {
            String statusAtual = rs.getString("status");

            if ("Vendido".equalsIgnoreCase(statusAtual)) {
                javax.swing.JOptionPane.showMessageDialog(null, "Produto já está VENDIDO!");
                sucesso = false; // Retorna falso para o botão não mostrar sucesso
            } else {
                // 2. Atualiza se não estiver vendido
                String sqlUpdate = "UPDATE produtos SET status = 'Vendido' WHERE id = ?";
                PreparedStatement prepUpdate = conn.prepareStatement(sqlUpdate);
                prepUpdate.setInt(1, id);
                prepUpdate.executeUpdate();
                sucesso = true; // Retorna verdadeiro
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(null, "ID não encontrado!");
        }
        conecta.desconectar();
    } catch (SQLException e) {
        System.out.println("Erro: " + e.getMessage());
    }
    return sucesso;
}         
}
