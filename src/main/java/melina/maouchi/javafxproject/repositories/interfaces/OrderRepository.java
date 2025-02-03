package melina.maouchi.javafxproject.repositories.interfaces;

import melina.maouchi.javafxproject.models.entities.Customer;
import melina.maouchi.javafxproject.models.entities.Order;
import melina.maouchi.javafxproject.models.enums.OrderStatus;
import melina.maouchi.javafxproject.repositories.GenericRepository;

import java.util.List;

public interface OrderRepository extends GenericRepository<Order, Integer> {
    List<Order> findByCustomer(Customer customer);
    List<Order> findByStatus(OrderStatus status);
}