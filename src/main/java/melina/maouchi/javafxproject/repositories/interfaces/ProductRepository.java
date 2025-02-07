package melina.maouchi.javafxproject.repositories.interfaces;

import melina.maouchi.javafxproject.models.entities.Product;
import melina.maouchi.javafxproject.repositories.GenericRepository;

import java.util.List;

public interface ProductRepository extends GenericRepository<Product, Integer> {
    //List<Product> searchByKeyword(String keyword);
    List<Product> findByCategory(String category);
    List<Product> searchProducts(String query);

}
