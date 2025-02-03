package melina.maouchi.javafxproject.repositories.interfaces;

import melina.maouchi.javafxproject.models.entities.Customer;
import melina.maouchi.javafxproject.repositories.GenericRepository;

import java.util.Optional;

public interface CustomerRepository extends GenericRepository<Customer, Integer> {
    Optional<Customer> findByEmail(String email);
}