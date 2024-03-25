
import java.sql.PreparedStatement;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProdutosDAO {

    Connection conn;
    PreparedStatement prep;
    ResultSet resultset;

    public void cadastrarProduto(ProdutosDTO produto) throws Exception {
        conn = new conectaDAO().connectDB();
        
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO produtos (nome, valor, status) VALUES (?, ?, ?)");
        stmt.setString(1, produto.getNome());
        stmt.setInt(2, produto.getValor());
        stmt.setString(3, produto.getStatus());
        stmt.executeUpdate();
        stmt.close();
        
        conn.close();
    }

    public List<ProdutosDTO> listarProdutos() throws Exception {
        conn = new conectaDAO().connectDB();

        List<ProdutosDTO> produtos = new ArrayList();
        
        PreparedStatement stmt = conn.prepareStatement("SELECT id, nome, valor, status FROM produtos");
        ResultSet resultSet = stmt.executeQuery();
        while(resultSet.next()) {
            ProdutosDTO produto = new ProdutosDTO();
            produto.setId(resultSet.getInt("id"));
            produto.setNome(resultSet.getString("nome"));
            produto.setValor(resultSet.getInt("valor"));
            produto.setStatus(resultSet.getString("status"));
            produtos.add(produto);
        }
        stmt.close();
        conn.close();
        
        return produtos;
    }

    public void venderProduto(ProdutosDTO produto) throws Exception {
        conn = new conectaDAO().connectDB();
        
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE produtos (id, nome, valor, status) VALUES (?, ?, ?, 'Vendido')")) {
            stmt.setString(1, produto.getNome());
            stmt.setInt(2, produto.getValor());
            stmt.setString(3, produto.getStatus());
            stmt.setInt(4, produto.getId());
            stmt.executeUpdate();
        }
        
        conn.close();
    }    
    
    public List<ProdutosDTO> listarProdutosVendidos() throws Exception {
        conn = new conectaDAO().connectDB();

        List<ProdutosDTO> produtos = new ArrayList();
        
        try (PreparedStatement stmt = conn.prepareStatement("SELECT id, nome, valor, status FROM produtos WHERE status LIKE 'Vendido'")) {
            ResultSet resultSet = stmt.executeQuery();
            while(resultSet.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(resultSet.getInt("id"));
                produto.setNome(resultSet.getString("nome"));
                produto.setValor(resultSet.getInt("valor"));
                produto.setStatus(resultSet.getString("status"));
                produtos.add(produto);
            }
        }
        conn.close();
        
        return produtos;
    }
    
}
