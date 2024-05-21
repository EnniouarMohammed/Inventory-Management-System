package ma.xproce.inventorymanagementsystem.dao.repositories;

import ma.xproce.inventorymanagementsystem.dao.entities.Customer;
import ma.xproce.inventorymanagementsystem.dao.entities.UserIMS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Page<Customer> findByFullNameContainsAndCreatedBy(String keyword, UserIMS user, Pageable pageable);

    List<Customer> findByCreatedBy(UserIMS user);
}