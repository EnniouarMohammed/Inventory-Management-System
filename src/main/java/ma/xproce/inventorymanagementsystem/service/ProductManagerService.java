package ma.xproce.inventorymanagementsystem.service;

import ma.xproce.inventorymanagementsystem.config.SecurityUtil;
import ma.xproce.inventorymanagementsystem.dao.entities.Category;
import ma.xproce.inventorymanagementsystem.dao.entities.Customer;
import ma.xproce.inventorymanagementsystem.dao.entities.Product;
import ma.xproce.inventorymanagementsystem.dao.entities.UserIMS;
import ma.xproce.inventorymanagementsystem.dao.repositories.ProductRepository;
import ma.xproce.inventorymanagementsystem.dao.repositories.UserIMSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class ProductManagerService implements ProductManager{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserIMSRepository userIMSRepository;

    private UserIMS getCurrentUser() {
        String email = SecurityUtil.getSessionUser();
        return userIMSRepository.findUserIMSByEmail(email);
    }

    @Override
    public Product updateProduct(Product product) {
        try{
            UserIMS user = getCurrentUser();
            if (product.getCreatedBy().equals(user)){
                return productRepository.save(product);
            }
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            return null;
        }
        return null;
    }

    @Override
    public boolean deleteProductById(Integer id) {
        try {
            UserIMS user = getCurrentUser();
            Product product = productRepository.findById(id).orElse(null);
            if (product != null && product.getCreatedBy().equals(user)) {
                productRepository.deleteById(id);
                return true;
            }
        } catch (Exception exception) {
            return false;
        }
        return false;
    }

    @Override
    public Product findProductById(int id) {
        try{
            UserIMS user = getCurrentUser();
            Product product = productRepository.findById(id).orElse(null);
            if (product != null && product.getCreatedBy().equals(user)) {
                return product;
            }
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            return null;
        }
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        try{
            UserIMS user = getCurrentUser();
            return productRepository.findByCreatedBy(user);
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            return null;
        }
    }

    @Override
    public Page<Product> searchProducts(String keyword, UserIMS user, int page, int taille) {
        return productRepository.findByNameContainsAndCreatedBy(keyword, user, PageRequest.of(page, taille));
    }

    @Override
    public Product saveProduct(MultipartFile image, String name, String code, Category category, double cost, double price, int quantity, String description) {
        UserIMS user = getCurrentUser();
        Product p = new Product();
        try {
            p.setImage(Base64.getEncoder().encodeToString(image.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        p.setName(name);
        p.setCode(code);
        p.setCategory(category);
        p.setCost(cost);
        p.setPrice(price);
        p.setQuantity(quantity);
        p.setDescription(description);
        p.setCreatedBy(user);

        return productRepository.save(p);
    }

    @Override
    public Product editProduct(int id, MultipartFile image, String name, String code, Category category, double cost, double price, int quantity, String description) {
        UserIMS user = getCurrentUser();
        Product p = findProductById(id);

        if (p != null && p.getCreatedBy().equals(user)) {
            if (image != null && !image.isEmpty()) {
                try {
                    p.setImage(Base64.getEncoder().encodeToString(image.getBytes()));
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                p.setImage(p.getImage());
            }

            p.setName(name);
            p.setCode(code);
            p.setCategory(category);
            p.setCost(cost);
            p.setPrice(price);
            p.setQuantity(quantity);
            p.setDescription(description);
        }

        return productRepository.save(p);
    }

}
