package ma.xproce.inventorymanagementsystem.web;

import ma.xproce.inventorymanagementsystem.config.SecurityUtil;
import ma.xproce.inventorymanagementsystem.dao.entities.Category;
import ma.xproce.inventorymanagementsystem.dao.entities.Product;
import ma.xproce.inventorymanagementsystem.dao.entities.UserIMS;
import ma.xproce.inventorymanagementsystem.service.CategoryManager;
import ma.xproce.inventorymanagementsystem.service.ProductManager;
import ma.xproce.inventorymanagementsystem.service.UserIMSManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    ProductManager productManager;
    @Autowired
    CategoryManager categoryManager;
    @Autowired
    UserIMSManager userIMSManager;

    /**************************************************************Product List*/
    @GetMapping("/page-list-product")
    public String ListProduct(Model model,
                              @RequestParam(name = "page", defaultValue = "0") int page,
                              @RequestParam(name = "taille", defaultValue = "6") int taille,
                              @RequestParam(name = "search", defaultValue = "") String keyword) {
        String username = SecurityUtil.getSessionUser();
        UserIMS user = userIMSManager.findUserIMSByEmail(username);

        Page<Product> products = productManager.searchProducts(keyword, user, page, taille);
        model.addAttribute("listProducts", products.getContent());
        int[] pages = new int[products.getTotalPages()];
        model.addAttribute("pages", pages);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        return "page-list-product";
    }

    /**************************************************************ADD Product*/

    @GetMapping("/page-add-product")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        List<Category> allCategories = categoryManager.getAllCategories();
        model.addAttribute("allCategories", allCategories);
        return "page-add-product";
    }

    @PostMapping("/page-add-product")
    public String addProduct(@RequestParam("image") MultipartFile image,
                             @RequestParam("name") String name,
                             @RequestParam("code") String code,
                             @RequestParam("category") Category category,
                             @RequestParam("cost") double cost,
                             @RequestParam("price") double price,
                             @RequestParam("quantity") int quantity,
                             @RequestParam("description") String description,
                             Model model) {
        productManager.saveProduct(image, name, code,category,cost,price,quantity,description);
        return "redirect:/page-list-product";
    }

    /**************************************************************Delete Product*/
    @GetMapping("/deleteProduct")
    public String deleteProduct(Model model, @RequestParam(name = "id") Integer id) {
        if (productManager.deleteProductById(id)) {
            return "redirect:page-list-product";
        } else {
            return "/page-error";
        }
    }

    /**************************************************************Edit Product*/
    @GetMapping("/page-edit-product/{id}")
    public String editProduct(@PathVariable("id") int id, Model model) {
        Product product = productManager.findProductById(id);
        List<Category> allCategories = categoryManager.getAllCategories();

        model.addAttribute("product", product);
        model.addAttribute("allCategories", allCategories);

        return "page-edit-product";
    }

    @PostMapping("/page-edit-product/{id}")
    public String updateProduct(@PathVariable("id") int id,
                                @RequestParam("image") MultipartFile image,
                                @RequestParam("name") String name,
                                @RequestParam("code") String code,
                                @RequestParam("category") Category category,
                                @RequestParam("cost") double cost,
                                @RequestParam("price") double price,
                                @RequestParam("quantity") int quantity,
                                @RequestParam("description") String description,
                                Model model) {
        productManager.editProduct(id, image, name, code, category, cost, price, quantity, description);
        return "redirect:/page-list-product";
    }

}
