package ma.xproce.inventorymanagementsystem.dao.repositories;

import jakarta.transaction.Transactional;
import ma.xproce.inventorymanagementsystem.dao.entities.Product;
import ma.xproce.inventorymanagementsystem.dao.entities.UserIMS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Transactional
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findByNameContainsAndCreatedBy(String keyword,UserIMS user, Pageable pageable);
    List<Product> findByCreatedBy(UserIMS user);
    @Query("SELECT COALESCE(COUNT(p),0) FROM Product p")
    int totalProducts();


}