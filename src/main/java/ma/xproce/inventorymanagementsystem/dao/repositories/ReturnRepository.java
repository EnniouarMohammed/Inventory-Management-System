package ma.xproce.inventorymanagementsystem.dao.repositories;

import jakarta.transaction.Transactional;
import ma.xproce.inventorymanagementsystem.dao.entities.Return;
import ma.xproce.inventorymanagementsystem.dao.entities.UserIMS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Transactional
public interface ReturnRepository  extends JpaRepository<Return, Integer> {
    Page<Return> findByReturnReferenceContainsAndCreatedBy(String keyword,UserIMS user, Pageable pageable);

    List<Return> findByCreatedBy(UserIMS user);
}