package melina.maouchi.javafxproject.repositories.interfaces;

import melina.maouchi.javafxproject.models.entities.Invoice;
import melina.maouchi.javafxproject.models.entities.Order;
import melina.maouchi.javafxproject.repositories.GenericRepository;

import java.util.Optional;

public interface InvoiceRepository extends GenericRepository<Invoice, Integer> {
    Optional<Invoice> findByOrder(Order order);
}