package ma.xproce.inventorymanagementsystem.web;

import jakarta.validation.Valid;
import ma.xproce.inventorymanagementsystem.config.SecurityUtil;
import ma.xproce.inventorymanagementsystem.dao.entities.Product;
import ma.xproce.inventorymanagementsystem.dao.entities.Return;
import ma.xproce.inventorymanagementsystem.dao.entities.Sale;
import ma.xproce.inventorymanagementsystem.dao.entities.UserIMS;
import ma.xproce.inventorymanagementsystem.service.ProductManager;
import ma.xproce.inventorymanagementsystem.service.ReturnManager;
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
public class ReturnController {
    @Autowired
    SaleManager saleManager;
    @Autowired
    ReturnManager returnManager;
    @Autowired
    ProductManager productManager;
    @Autowired
    UserIMSManager userIMSManager;

    /************************************************************Return List*/
    @GetMapping("/page-list-returns")
    public String ListReturn(Model model,
                           @RequestParam(name = "page", defaultValue = "0") int page,
                           @RequestParam(name = "taille", defaultValue = "6") int taille,
                           @RequestParam(name = "search", defaultValue = "") String keyword) {
        String username = SecurityUtil.getSessionUser();
        UserIMS user = userIMSManager.findUserIMSByEmail(username);

        Page<Return> rt = returnManager.searchReturns(keyword, user, page, taille);
        model.addAttribute("listReturns", rt.getContent());
        int[] pages = new int[rt.getTotalPages()];
        model.addAttribute("pages", pages);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        return "page-list-returns";
    }

    /************************************************************Add Return*/
    @GetMapping("/page-add-return")
    public String showAddReturnForm(Model model) {
        model.addAttribute("return", new Return());
        List<Sale> allSales = saleManager.getAllSales();
        List<Return> allReturns = returnManager.getAllReturns();
        Return rt = new Return();
        rt.generateReturnReference();

        model.addAttribute("allReturns", allReturns);
        model.addAttribute("allSales", allSales);
        model.addAttribute("ref", rt.getReturnReference());
        return "page-add-return";
    }

    @PostMapping("/page-add-return")
    public String addReturn(Model model, @Valid Return rt, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.toString());
            return "page-add-return";
        }
        returnManager.addReturn(rt);

        Sale sale = rt.getSale();
        Product product = sale.getProduct();

        int returnedQuantity = sale.getQuantity();
        int currentProductQuantity = product.getQuantity();
        product.setQuantity(currentProductQuantity + returnedQuantity);

        sale.setRefunded(true);

        saleManager.updateSale(sale);
        productManager.updateProduct(product);

        return "redirect:page-list-returns";
    }

    /************************************************************Delete Return*/
    @GetMapping("/deleteReturn")
    public String deleteReturn(Model model, @RequestParam(name = "id") Integer id) {
        if (returnManager.deleteReturnById(id)) {
            return "redirect:page-list-returns";
        } else {
            return "/page-error";
        }
    }

    /************************************************************Edit Return*/
    @GetMapping("/page-edit-return/{id}")
    public String editReturn(@PathVariable("id") int id, Model model) {
        Return rt = returnManager.findReturnById(id);
        List<Sale> allSales = saleManager.getAllSales();

        model.addAttribute("rt", rt);
        model.addAttribute("allSales", allSales);

        return "page-edit-return";
    }

    @PostMapping("/page-edit-return/{id}")
    public String updateReturn(@PathVariable("id") int id, @ModelAttribute("rt") Return rt) {
        returnManager.updateReturn(rt);
        return "redirect:/page-list-returns";
    }
}
