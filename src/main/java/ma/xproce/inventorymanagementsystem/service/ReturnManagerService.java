package ma.xproce.inventorymanagementsystem.service;

import ma.xproce.inventorymanagementsystem.config.SecurityUtil;
import ma.xproce.inventorymanagementsystem.dao.entities.Customer;
import ma.xproce.inventorymanagementsystem.dao.entities.Return;
import ma.xproce.inventorymanagementsystem.dao.entities.UserIMS;
import ma.xproce.inventorymanagementsystem.dao.repositories.ReturnRepository;
import ma.xproce.inventorymanagementsystem.dao.repositories.UserIMSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReturnManagerService implements ReturnManager {
    @Autowired
    private ReturnRepository returnRepository;
    @Autowired
    private UserIMSRepository userIMSRepository;

    private UserIMS getCurrentUser() {
        String email = SecurityUtil.getSessionUser();
        return userIMSRepository.findUserIMSByEmail(email);
    }

    @Override
    public Return addReturn(Return returnObj) {
        try {
            UserIMS user = getCurrentUser();
            returnObj.setCreatedBy(user);
            return returnRepository.save(returnObj);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    @Override
    public Return updateReturn(Return returnObj) {
        try{
            UserIMS user = getCurrentUser();
            if (returnObj.getCreatedBy().equals(user)){
                return returnRepository.save(returnObj);
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return null;
        }
        return null;
    }

    @Override
    public boolean deleteReturnById(Integer id) {
        try {
            UserIMS user = getCurrentUser();
            Return rt = returnRepository.findById(id).orElse(null);
            if (rt != null && rt.getCreatedBy().equals(user)) {
                returnRepository.deleteById(id);
                return true;
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return false;
        }
        return false;
    }

    @Override
    public Return findReturnById(int id) {
        try {
            UserIMS user = getCurrentUser();
            Return rt = returnRepository.findById(id).orElse(null);
            if (rt != null && rt.getCreatedBy().equals(user)) {
                return rt;
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return null;
        }
        return null;
    }

    @Override
    public List<Return> getAllReturns() {
        try {
            UserIMS user = getCurrentUser();
            return returnRepository.findByCreatedBy(user);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    @Override
    public Page<Return> searchReturns(String keyword,UserIMS user, int page, int taille) {
        return returnRepository.findByReturnReferenceContainsAndCreatedBy(keyword, user, PageRequest.of(page, taille));
    }
}
