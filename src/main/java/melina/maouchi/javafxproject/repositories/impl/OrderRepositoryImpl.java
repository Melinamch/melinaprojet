package melina.maouchi.javafxproject.repositories.impl;

import melina.maouchi.javafxproject.config.DatabaseConnection;
import melina.maouchi.javafxproject.models.entities.Customer;
import melina.maouchi.javafxproject.models.entities.Order;
import melina.maouchi.javafxproject.models.entities.OrderItem;
import melina.maouchi.javafxproject.models.entities.Product;
import melina.maouchi.javafxproject.models.enums.OrderStatus;
import melina.maouchi.javafxproject.repositories.interfaces.CustomerRepository;
import melina.maouchi.javafxproject.repositories.interfaces.OrderRepository;
import melina.maouchi.javafxproject.repositories.interfaces.ProductRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepositoryImpl implements OrderRepository {
    private Connection connection;
    private CustomerRepository customerRepository;
    private ProductRepository productRepository;

    public OrderRepositoryImpl(CustomerRepository customerRepository,
                               ProductRepository productRepository) {
        try {
            this.connection = DatabaseConnection.getConnection();
            this.customerRepository = customerRepository;
            this.productRepository = productRepository;
        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed", e);
        }
    }

    public OrderRepositoryImpl() {

    }


    @Override
    public void add(Order order) {
        String sql = "INSERT INTO orders (customer_id, order_date, status, total_amount) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, order.getCustomer().getId());
            stmt.setTimestamp(2, Timestamp.valueOf(order.getOrderDate().atStartOfDay()));
            stmt.setString(3, order.getStatus().name());
            stmt.setDouble(4, order.getTotalAmount());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setId(generatedKeys.getInt(1));
                }
            }

            addOrderItems(order);
        } catch (SQLException e) {
            throw new RuntimeException("Error adding order", e);
        }
    }

    private void addOrderItems(Order order) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (OrderItem item : order.getOrderItems()) {
                stmt.setInt(1, order.getId());
                stmt.setInt(2, item.getProduct().getId());
                stmt.setInt(3, item.getQuantity());
                stmt.setDouble(4, item.getProduct().getPrice());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    @Override
    public void update(Order order) {
        String sql = "UPDATE orders SET customer_id=?, status=?, total_amount=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, order.getCustomer().getId());
            stmt.setString(2, order.getStatus().name());
            stmt.setDouble(3, order.getTotalAmount());
            stmt.setInt(4, order.getId());
            stmt.executeUpdate();

            deleteOrderItems(order.getId());
            addOrderItems(order);
        } catch (SQLException e) {
            throw new RuntimeException("Error updating order", e);
        }
    }

    private void deleteOrderItems(int orderId) throws SQLException {
        String sql = "DELETE FROM order_items WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        deleteOrderItems(id);

        String sql = "DELETE FROM orders WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting order", e);
        }
    }

    @Override
    public Optional<Order> findById(Integer id) {
        String sql = "SELECT * FROM orders WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getInt("id"));

                    int customerId = rs.getInt("customer_id");
                    order.setCustomer(customerRepository.findById(customerId)
                            .orElseThrow(() -> new RuntimeException("Customer not found")));

                    order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime().toLocalDate());
                    order.setStatus(OrderStatus.valueOf(rs.getString("status")));
                    order.setTotalAmount(rs.getDouble("total_amount"));

                    order.setOrderItems(fetchOrderItems(order));

                    return Optional.of(order);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding order", e);
        }
        return Optional.empty();
    }

    private List<OrderItem> fetchOrderItems(Order order) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, order.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    int productId = rs.getInt("product_id");
                    Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new RuntimeException("Product not found"));

                    item.setProduct(product);
                    item.setQuantity(rs.getInt("quantity"));
                    items.add(item);
                }
            }
        }
        return items;
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Optional<Order> order = findById(rs.getInt("id"));
                order.ifPresent(orders::add);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all orders", e);
        }
        return orders;
    }

    @Override
    public List<Order> findByCustomer(Customer customer) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE customer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, customer.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Optional<Order> order = findById(rs.getInt("id"));
                    order.ifPresent(orders::add);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding orders by customer", e);
        }
        return orders;
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE status = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Optional<Order> order = findById(rs.getInt("id"));
                    order.ifPresent(orders::add);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding orders by status", e);
        }
        return orders;
    }
}
