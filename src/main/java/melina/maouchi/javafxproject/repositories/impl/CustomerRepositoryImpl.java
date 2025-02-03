package melina.maouchi.javafxproject.repositories.impl;

import melina.maouchi.javafxproject.config.DatabaseConnection;
import melina.maouchi.javafxproject.models.entities.Customer;
import melina.maouchi.javafxproject.repositories.interfaces.CustomerRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerRepositoryImpl implements CustomerRepository {
    private Connection connection;

    public CustomerRepositoryImpl() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed", e);
        }
    }

    @Override
    public void add(Customer customer) {
        String sql = "INSERT INTO customers (first_name, last_name, email, phone) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPhone());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    customer.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding customer", e);
        }
    }

    @Override
    public void update(Customer customer) {
        String sql = "UPDATE customers SET first_name=?, last_name=?, email=?, phone=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPhone());
            stmt.setInt(5, customer.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating customer", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM customers WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting customer", e);
        }
    }

    @Override
    public Optional<Customer> findById(Integer id) {
        String sql = "SELECT * FROM customers WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Customer customer = new Customer();
                    customer.setId(rs.getInt("id"));
                    customer.setFirstName(rs.getString("first_name"));
                    customer.setLastName(rs.getString("last_name"));
                    customer.setEmail(rs.getString("email"));
                    customer.setPhone(rs.getString("phone"));
                    return Optional.of(customer);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding customer", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setFirstName(rs.getString("first_name"));
                customer.setLastName(rs.getString("last_name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customers.add(customer);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all customers", e);
        }
        return customers;
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        String sql = "SELECT * FROM customers WHERE email=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Customer customer = new Customer();
                    customer.setId(rs.getInt("id"));
                    customer.setFirstName(rs.getString("first_name"));
                    customer.setLastName(rs.getString("last_name"));
                    customer.setEmail(rs.getString("email"));
                    customer.setPhone(rs.getString("phone"));
                    return Optional.of(customer);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding customer by email", e);
        }
        return Optional.empty();
    }
}