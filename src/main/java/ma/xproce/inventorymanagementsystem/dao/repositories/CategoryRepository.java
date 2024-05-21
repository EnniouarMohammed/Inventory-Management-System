package ma.xproce.inventorymanagementsystem.dao.repositories;

import jakarta.transaction.Transactional;
import ma.xproce.inventorymanagementsystem.dao.entities.Category;
import ma.xproce.inventorymanagementsystem.dao.entities.Product;
import ma.xproce.inventorymanagementsystem.dao.entities.UserIMS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Transactional
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Page<Category> findByCategoryNameContainsAndCreatedBy(String keyword, UserIMS createdBy, Pageable pageable);
    List<Category> findByCreatedBy(UserIMS createdBy);
}
