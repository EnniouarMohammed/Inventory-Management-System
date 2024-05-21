package ma.xproce.inventorymanagementsystem.web;

import jakarta.validation.Valid;
import ma.xproce.inventorymanagementsystem.config.SecurityUtil;
import ma.xproce.inventorymanagementsystem.dao.entities.Customer;
import ma.xproce.inventorymanagementsystem.dao.entities.Product;
import ma.xproce.inventorymanagementsystem.dao.entities.Sale;
import ma.xproce.inventorymanagementsystem.dao.entities.UserIMS;
import ma.xproce.inventorymanagementsystem.service.CustomerManager;
import ma.xproce.inventorymanagementsystem.service.ProductManager;
import ma.xproce.inventorymanagementsystem.service.SaleManager;
import ma.xproce.inventorymanagementsystem.service.UserIMSManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class SaleController {
    @Autowired
    SaleManager saleManager;
    @Autowired
    ProductManager productManager;
    @Autowired
    CustomerManager customerManager;
    @Autowired
    UserIMSManager userIMSManager;

    @GetMapping("/page-list-sale")
    public String ListSale(Model model,
                              @RequestParam(name = "page", defaultValue = "0") int page,
                              @RequestParam(name = "taille", defaultValue = "6") int taille,
                              @RequestParam(name = "search", defaultValue = "") String keyword) {
        String username = SecurityUtil.getSessionUser();
        UserIMS user = userIMSManager.findUserIMSByEmail(username);

        Page<Sale> sales = saleManager.searchSales(keyword, user, page, taille);
        model.addAttribute("listSales", sales.getContent());
        int[] pages = new int[sales.getTotalPages()];
        model.addAttribute("pages", pages);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        return "page-list-sale";
    }

    @GetMapping("/page-add-sale")
    public String showAddSaleForm(Model model) {
        model.addAttribute("sale", new Sale());
        List<Sale> allSales = saleManager.getAllSales();
        List<Product> allProducts = productManager.getAllProducts();
        List<Customer> allCustomers = customerManager.getAllCustomers();
        Sale sale = new Sale();
        sale.generateReference();

        model.addAttribute("allSales", allSales);
        model.addAttribute("allProducts", allProducts);
        model.addAttribute("allCustomers", allCustomers);
        model.addAttribute("ref", sale.getReference());
        return "page-add-sale";
    }

    @PostMapping("/page-add-sale")
    public String addSale(Model model, @Valid Sale sale, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.toString());
            return "page-add-sale";
        }
        saleManager.addSale(sale);
        int soldQuantity = sale.getQuantity();
        Product product = sale.getProduct();
        int updatedQuantity = product.getQuantity() - soldQuantity;
        product.setQuantity(updatedQuantity);
        productManager.updateProduct(product);

        return "redirect:page-list-sale";
    }

    @GetMapping("/deleteSale")
    public String deleteSale(Model model, @RequestParam(name = "id") Integer id) {
        if (saleManager.deleteSaleById(id)) {
            return "redirect:page-list-sale";
        } else {
            return "/page-error";
        }
    }

    @GetMapping("/page-edit-sale/{id}")
    public String editSale(@PathVariable("id") int id, Model model) {
        Sale sale = saleManager.findSaleById(id);
        List<Product> allProducts = productManager.getAllProducts();
        List<Customer> allCustomers = customerManager.getAllCustomers();

        model.addAttribute("sale", sale);
        model.addAttribute("allProducts", allProducts);
        model.addAttribute("allCustomers", allCustomers);

        int soldQuantity = sale.getQuantity();
        Product product = sale.getProduct();
        int productQuantity = product.getQuantity();
        int updatedQuantity = productQuantity + soldQuantity;

        product.setQuantity(updatedQuantity);

        return "page-edit-sale";
    }

    @PostMapping("/page-edit-sale/{id}")
    public String updateSale(@PathVariable("id") int id, @ModelAttribute("sale") Sale sale) {
        saleManager.updateSale(sale);
        Product product = sale.getProduct();
        int productQuantity = product.getQuantity();
        int updatedQuantity = productQuantity - sale.getQuantity();
        product.setQuantity(updatedQuantity);
        return "redirect:/page-list-sale";
    }
}