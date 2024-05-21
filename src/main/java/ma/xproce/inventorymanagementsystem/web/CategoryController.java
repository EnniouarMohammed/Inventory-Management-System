package ma.xproce.inventorymanagementsystem.web;

import ma.xproce.inventorymanagementsystem.config.SecurityUtil;
import ma.xproce.inventorymanagementsystem.service.UserIMSManager;
import org.springframework.ui.Model;
import ma.xproce.inventorymanagementsystem.dao.entities.Category;
import ma.xproce.inventorymanagementsystem.dao.entities.UserIMS;
import ma.xproce.inventorymanagementsystem.service.CategoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class CategoryController {
    @Autowired
    CategoryManager categoryManager;
    @Autowired
    UserIMSManager userIMSManager;

    /**************************************************************Category List*/
    @GetMapping("/page-list-category")
    public String listCategory(org.springframework.ui.Model model,
                               @RequestParam(name = "page", defaultValue = "0") int page,
                               @RequestParam(name = "taille", defaultValue = "10") int taille,
                               @RequestParam(name = "search", defaultValue = "") String keyword) {
        String username = SecurityUtil.getSessionUser();
        UserIMS user = userIMSManager.findUserIMSByEmail(username);

        Page<Category> categories = categoryManager.searchCategories(keyword, user, page, taille);
        model.addAttribute("listCategories", categories.getContent());
        int[] pages = new int[categories.getTotalPages()];
        model.addAttribute("pages", pages);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        return "page-list-category";
    }

    /**************************************************************Add Category*/
    @PostMapping("/page-add-category")
    public String addCategory(Model model,
                                       @RequestParam("image") MultipartFile image,
                                       @RequestParam(name = "categoryName") String categoryName) {
        categoryManager.saveCategory(image,categoryName);
        return "redirect:page-list-category";
    }

    @GetMapping("/page-add-category")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "page-add-category";
    }

    /**************************************************************Delete Category*/
    @GetMapping("/deleteCategory")
    public String deleteCategory(Model model, @RequestParam(name = "id") Integer id) {
        if (categoryManager.deleteCategoryById(id)) {
            return "redirect:page-list-category";
        } else {
            return "/page-error";
        }
    }

    /**************************************************************Edit Category*/
    @GetMapping("/page-edit-category/{id}")
    public String editCategory(@PathVariable("id") int id, Model model) {
        Category category = categoryManager.findCategoryById(id);
        model.addAttribute("category", category);
        return "page-edit-category";
    }

    @PostMapping("/page-edit-category/{id}")
    public String updateCategory(Model model,
                                 @PathVariable("id") int id,
                                 @RequestParam("image") MultipartFile image,
                                 @RequestParam(name = "categoryName") String categoryName) {
        categoryManager.editCategory(id, image, categoryName);
        return "redirect:/page-list-category";
    }
}
