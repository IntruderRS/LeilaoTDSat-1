
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class ProdutosDAO {

    Connection conn;
    PreparedStatement prep;
    ResultSet resultset;
    ArrayList<ProdutosDTO> listagem = new ArrayList<>();

    public ArrayList<ProdutosDTO> listarProdutosVendidos() {
        String sql = "SELECT * FROM produtos WHERE status = 'Vendido'";
        conectaDAO conecta = new conectaDAO();
        conn = conecta.conectar();

        listagem.clear();

        try {
            prep = conn.prepareStatement(sql);
            resultset = prep.executeQuery();

            while (resultset.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(resultset.getInt("id"));
                produto.setNome(resultset.getString("nome"));
                produto.setValor(resultset.getInt("valor"));
                produto.setStatus(resultset.getString("status"));

                listagem.add(produto);
            }
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao listar vendidos: " + erro.getMessage());
        } finally {
            conecta.desconectar();
        }
        return listagem;
    }

    public void cadastrarProduto(ProdutosDTO produto) {
        conectaDAO conecta = new conectaDAO();
        conn = conecta.conectar();

        String sql = "INSERT INTO produtos (nome, valor, status) VALUES (?, ?, ?)";

        try {
            prep = conn.prepareStatement(sql);
            prep.setString(1, produto.getNome());
            prep.setDouble(2, produto.getValor());
            prep.setString(3, produto.getStatus());

            prep.executeUpdate();
            

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar no banco: " + e.getMessage());
        } finally {
            conecta.desconectar();
        }
    }

    public ArrayList<ProdutosDTO> listarProdutos() {

        return listagem;
    }

    public boolean venderProduto(int id) {
        conectaDAO conecta = new conectaDAO();
        conn = conecta.conectar();
        boolean sucesso = false;

        try {
            // AQUI O STATUS SERÁ VERIFICADO
            String sqlCheck = "SELECT status FROM produtos WHERE id = ?";
            PreparedStatement prepCheck = conn.prepareStatement(sqlCheck);
            prepCheck.setInt(1, id);
            ResultSet rs = prepCheck.executeQuery();

            if (rs.next()) {
                String statusAtual = rs.getString("status");
                //PERCEBI QUE PODIA VENDER UM PRODUTO JÁ VENDIDO, AQUI FAZ A VERIFICAÇÃO ANTES
                if ("Vendido".equalsIgnoreCase(statusAtual)) {
                    JOptionPane.showMessageDialog(null, "Produto já está VENDIDO!");
                    
                    sucesso = false;
                } else {
                    // 2. Atualiza se não estiver vendido
                    String sqlUpdate = "UPDATE produtos SET status = 'Vendido' WHERE id = ?";
                    PreparedStatement prepUpdate = conn.prepareStatement(sqlUpdate);
                    prepUpdate.setInt(1, id);
                    prepUpdate.executeUpdate();
                    sucesso = true; // Retorna verdadeiro
                }
            } else {
                JOptionPane.showMessageDialog(null, "ID não encontrado!");
            }
            conecta.desconectar();
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }finally {
            conecta.desconectar();
        }
        return sucesso;
    }
}
