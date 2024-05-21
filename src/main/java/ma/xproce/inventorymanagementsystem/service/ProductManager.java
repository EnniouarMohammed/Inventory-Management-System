package ma.xproce.inventorymanagementsystem.service;

import ma.xproce.inventorymanagementsystem.dao.entities.Category;
import ma.xproce.inventorymanagementsystem.dao.entities.Product;
import ma.xproce.inventorymanagementsystem.dao.entities.UserIMS;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public interface ProductManager {
    Product updateProduct(Product product);
    boolean deleteProductById(Integer id);

    Product findProductById(int id);

    List<Product> getAllProducts();

    public Page<Product> searchProducts(String keyword, UserIMS user, int page, int taille);

    Product saveProduct(MultipartFile image, String name, String code, Category category, double cost, double price, int quantity, String description);
    Product editProduct(int id, MultipartFile image, String name, String code, Category category, double cost, double price, int quantity, String description);
}
