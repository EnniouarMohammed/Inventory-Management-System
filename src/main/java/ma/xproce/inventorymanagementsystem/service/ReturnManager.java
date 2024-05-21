package ma.xproce.inventorymanagementsystem.service;

import ma.xproce.inventorymanagementsystem.dao.entities.Return;
import ma.xproce.inventorymanagementsystem.dao.entities.UserIMS;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ReturnManager {
    Return addReturn(Return Return);

    Return updateReturn(Return Return);
    boolean deleteReturnById(Integer id);

    Return findReturnById(int id);

    List<Return> getAllReturns();

    public Page<Return> searchReturns(String keyword, UserIMS user, int page, int taille);
}
