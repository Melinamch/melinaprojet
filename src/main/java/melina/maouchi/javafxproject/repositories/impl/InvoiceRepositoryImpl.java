package melina.maouchi.javafxproject.repositories.impl;

import melina.maouchi.javafxproject.config.DatabaseConnection;
import melina.maouchi.javafxproject.models.entities.Invoice;
import melina.maouchi.javafxproject.models.entities.Order;
import melina.maouchi.javafxproject.repositories.interfaces.InvoiceRepository;
import melina.maouchi.javafxproject.repositories.interfaces.OrderRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InvoiceRepositoryImpl implements InvoiceRepository {
    private final Connection connection;
    private final OrderRepository orderRepository;

    public InvoiceRepositoryImpl(OrderRepository orderRepository) {
        try {
            this.connection = DatabaseConnection.getConnection();
            this.orderRepository = orderRepository;
        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed", e);
        }
    }

    @Override
    public void add(Invoice invoice) {
        String sql = "INSERT INTO invoices (order_id, invoice_date, total_amount) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, invoice.getOrder().getId());
            stmt.setTimestamp(2, Timestamp.valueOf(invoice.getInvoiceDate().atStartOfDay()));
            stmt.setDouble(3, invoice.getTotalAmount());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    invoice.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding invoice", e);
        }
    }

    @Override
    public void update(Invoice invoice) {
        String query = "UPDATE invoices SET order_id = ?, invoice_date = ?, total_amount = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, invoice.getOrder().getId());
            stmt.setDate(2, java.sql.Date.valueOf(invoice.getInvoiceDate()));
            stmt.setDouble(3, invoice.getTotalAmount());
            stmt.setInt(4, invoice.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM invoices WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting invoice", e);
        }
    }

    @Override
    public Optional<Invoice> findById(Integer id) {
        String sql = "SELECT * FROM invoices WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setId(rs.getInt("id"));

                    int orderId = rs.getInt("order_id");
                    invoice.setOrder(orderRepository.findById(orderId)
                            .orElseThrow(() -> new RuntimeException("Order not found")));

                    invoice.setInvoiceDate(rs.getTimestamp("invoice_date").toLocalDateTime().toLocalDate());
                    invoice.setTotalAmount(rs.getDouble("total_amount"));

                    return Optional.of(invoice);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding invoice", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Invoice> findAll() {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Optional<Invoice> invoice = findById(rs.getInt("id"));
                invoice.ifPresent(invoices::add);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all invoices", e);
        }
        return invoices;
    }

    @Override
    public Optional<Invoice> findByOrder(Order order) {
        String sql = "SELECT * FROM invoices WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, order.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return findById(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding invoice by order", e);
        }
        return Optional.empty();
    }
}