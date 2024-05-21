package ma.xproce.inventorymanagementsystem.service;

import ma.xproce.inventorymanagementsystem.dao.entities.Product;
import ma.xproce.inventorymanagementsystem.dao.entities.Sale;
import ma.xproce.inventorymanagementsystem.dao.entities.UserIMS;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SaleManager {
    Sale addSale(Sale sale);

    Sale updateSale(Sale sale);

    boolean deleteSaleById(Integer id);

    Sale findSaleById(int id);

    List<Sale> getAllSales();

    public Page<Sale> searchSales(String keyword, UserIMS user, int page, int taille);

}
