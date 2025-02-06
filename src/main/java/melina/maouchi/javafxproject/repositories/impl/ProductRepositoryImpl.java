package melina.maouchi.javafxproject.repositories.impl;

import melina.maouchi.javafxproject.config.DatabaseConnection;
import melina.maouchi.javafxproject.models.entities.Product;
import melina.maouchi.javafxproject.repositories.interfaces.ProductRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepositoryImpl implements ProductRepository {
    private Connection connection;

    public ProductRepositoryImpl() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed", e);
        }
    }

    @Override
    public void add(Product product) {
        String sql = "INSERT INTO products (name, price, category, stock) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setString(3, product.getCategory());
            stmt.setInt(4, product.getStock());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding product", e);
        }
    }

    @Override
    public void update(Product product) {
        String sql = "UPDATE products SET name=?, price=?, category=?, stock=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setString(3, product.getCategory());
            stmt.setInt(4, product.getStock());
            stmt.setInt(5, product.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating product", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM products WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting product", e);
        }
    }

    @Override
    public Optional<Product> findById(Integer id) {
        String sql = "SELECT * FROM products WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getDouble("price"));
                    product.setCategory(rs.getString("category"));
                    product.setStock(rs.getInt("stock"));
                    return Optional.of(product);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding product", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Product> findAll() {
        return searchByKeyword(""); // Utilise searchByKeyword pour retourner tous les produits
    }

    @Override
    public List<Product> searchByKeyword(String keyword) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE name LIKE ? OR category LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getDouble("price"));
                    product.setCategory(rs.getString("category"));
                    product.setStock(rs.getInt("stock"));
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching products", e);
        }
        return products;
    }





    @Override
    public List<Product> findByCategory(String category) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE category = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, category);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getDouble("price"));
                    product.setCategory(rs.getString("category"));
                    product.setStock(rs.getInt("stock"));
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding products by category", e);
        }
        return products;
    }
}
