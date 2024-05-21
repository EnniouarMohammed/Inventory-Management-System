package ma.xproce.inventorymanagementsystem.service;

import ma.xproce.inventorymanagementsystem.config.SecurityUtil;
import ma.xproce.inventorymanagementsystem.dao.entities.Return;
import ma.xproce.inventorymanagementsystem.dao.entities.Sale;
import ma.xproce.inventorymanagementsystem.dao.entities.UserIMS;
import ma.xproce.inventorymanagementsystem.dao.repositories.SaleRepository;
import ma.xproce.inventorymanagementsystem.dao.repositories.UserIMSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleManagerService implements SaleManager{
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private UserIMSRepository userIMSRepository;

    private UserIMS getCurrentUser() {
        String email = SecurityUtil.getSessionUser();
        return userIMSRepository.findUserIMSByEmail(email);
    }

    @Override
    public Sale addSale(Sale sale) {
        try {
            UserIMS user = getCurrentUser();
            sale.setCreatedBy(user);
            return saleRepository.save(sale);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    @Override
    public Sale updateSale(Sale sale) {
        try {
            UserIMS user = getCurrentUser();
            if (sale.getCreatedBy().equals(user)){
                return saleRepository.save(sale);
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return null;
        }
        return null;
    }

    @Override
    public boolean deleteSaleById(Integer id) {
        try {
            UserIMS user = getCurrentUser();
            Sale sale = saleRepository.findById(id).orElse(null);
            if (sale != null && sale.getCreatedBy().equals(user)) {
                saleRepository.deleteById(id);
                return true;
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return false;
        }
        return false;
    }

    @Override
    public Sale findSaleById(int id) {
        try {
            UserIMS user = getCurrentUser();
            Sale sale = saleRepository.findById(id).orElse(null);
            if (sale != null && sale.getCreatedBy().equals(user)) {
                return sale;
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return null;
        }
        return null;
    }

    @Override
    public List<Sale> getAllSales() {
        try {
            UserIMS user = getCurrentUser();
            return saleRepository.findByCreatedBy(user);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    @Override
    public Page<Sale> searchSales(String keyword, UserIMS user, int page, int taille) {
        return saleRepository.findByReferenceContainsAndCreatedBy(keyword, user, PageRequest.of(page, taille));
    }
}