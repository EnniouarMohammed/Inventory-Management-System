package ma.xproce.inventorymanagementsystem.service;

import ma.xproce.inventorymanagementsystem.config.SecurityUtil;
import ma.xproce.inventorymanagementsystem.dao.entities.Category;
import ma.xproce.inventorymanagementsystem.dao.entities.UserIMS;
import ma.xproce.inventorymanagementsystem.dao.repositories.CategoryRepository;
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
public class CategoryManagerService implements CategoryManager{
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserIMSRepository userIMSRepository;

    private UserIMS getCurrentUser() {
        String email = SecurityUtil.getSessionUser();
        return userIMSRepository.findUserIMSByEmail(email);
    }

    @Override
    public boolean deleteCategoryById(Integer id) {
        try {
            UserIMS user = getCurrentUser();
            Category category = categoryRepository.findById(id).orElse(null);
            if (category != null && category.getCreatedBy().equals(user)) {
                categoryRepository.deleteById(id);
                return true;
            }
        } catch (Exception exception) {
            return false;
        }
        return false;
    }

    @Override
    public Category findCategoryById(int id) {
        try{
            UserIMS user = getCurrentUser();
            Category category = categoryRepository.findById(id).orElse(null);
            if (category != null && category.getCreatedBy().equals(user)) {
                return category;
            }
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            return null;
        }
        return null;
    }

    @Override
    public List<Category> getAllCategories() {
        try{
            UserIMS user = getCurrentUser();
            return categoryRepository.findByCreatedBy(user);
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            return null;
        }
    }

    @Override
    public Page<Category> searchCategories(String keyword,UserIMS user, int page, int taille) {
        return categoryRepository.findByCategoryNameContainsAndCreatedBy(keyword, user, PageRequest.of(page, taille));
    }

    @Override
    public Category saveCategory(MultipartFile image, String categoryName) {
        UserIMS user = getCurrentUser();
        Category c = new Category();
        try {
            c.setImage(Base64.getEncoder().encodeToString(image.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        c.setCategoryName(categoryName);
        c.setCreatedBy(user);

        return categoryRepository.save(c);
    }

    @Override
    public Category editCategory(int id, MultipartFile image, String categoryName) {
        UserIMS user = getCurrentUser();
        Category c = findCategoryById(id);

        if (c != null && c.getCreatedBy().equals(user)) {
            if (image != null && !image.isEmpty()) {
                try {
                    c.setImage(Base64.getEncoder().encodeToString(image.getBytes()));
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                c.setImage(c.getImage());
            }

            c.setCategoryName(categoryName);

            return categoryRepository.save(c);
        }
        return null;
    }
}
