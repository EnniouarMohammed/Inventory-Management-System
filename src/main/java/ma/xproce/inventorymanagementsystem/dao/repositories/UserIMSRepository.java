package ma.xproce.inventorymanagementsystem.dao.repositories;

import jakarta.transaction.Transactional;
import ma.xproce.inventorymanagementsystem.dao.entities.UserIMS;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface UserIMSRepository extends JpaRepository<UserIMS, Integer> {
    UserIMS findUserIMSByEmail(String email);
}
