package ma.xproce.inventorymanagementsystem.service;

import ma.xproce.inventorymanagementsystem.dao.entities.Category;
import ma.xproce.inventorymanagementsystem.dao.entities.UserIMS;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public interface CategoryManager {

    boolean deleteCategoryById(Integer id);

    Category findCategoryById(int id);

    List<Category> getAllCategories();

    Page<Category> searchCategories(String keyword, UserIMS user, int page, int size);

    Category saveCategory(MultipartFile image, String categoryName);
    Category editCategory(int id, MultipartFile image, String categoryName);
}
