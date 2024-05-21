package ma.xproce.inventorymanagementsystem.web;

import jakarta.validation.Valid;
import ma.xproce.inventorymanagementsystem.config.SecurityUtil;
import ma.xproce.inventorymanagementsystem.dao.entities.Customer;
import ma.xproce.inventorymanagementsystem.dao.entities.UserIMS;
import ma.xproce.inventorymanagementsystem.service.UserIMSManager;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ma.xproce.inventorymanagementsystem.service.CustomerManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CustomersController {
    @Autowired
    CustomerManager customerManager;
    @Autowired
    UserIMSManager userIMSManager;

    /************************************************************Customer List*/
    @GetMapping("/page-list-customers")
    public String listCustomer(org.springframework.ui.Model model,
                               @RequestParam(name = "page", defaultValue = "0") int page,
                               @RequestParam(name = "size", defaultValue = "6") int size,
                               @RequestParam(name = "search", defaultValue = "") String keyword) {
        String username = SecurityUtil.getSessionUser();
        UserIMS user = userIMSManager.findUserIMSByEmail(username);

        Page<Customer> customers = customerManager.searchCustomers(keyword, user, page, size);
        model.addAttribute("listCustomers", customers.getContent());
        int[] pages = new int[customers.getTotalPages()];
        model.addAttribute("pages", pages);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        return "page-list-customers";
    }

    /************************************************************Add Customer*/
    @PostMapping("/page-add-customer")
    public String addCustomer(Model model, @Valid Customer customer) {
        customerManager.addCustomer(customer);
        return "redirect:page-list-customers";
    }

    @GetMapping("/page-add-customer")
    public String showAddCustomerForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "page-add-customer";
    }

    /************************************************************Delete Customer*/
    @GetMapping("/deleteCustomer")
    public String deleteCustomer(Model model, @RequestParam(name = "id") Integer id) {
        if (customerManager.deleteCustomerById(id)) {
            return "redirect:page-list-customers";
        } else {
            return "/page-error";
        }
    }

    /************************************************************Edit Customer*/
    @GetMapping("/page-edit-customer/{id}")
    public String editCustomer(@PathVariable("id") int id, Model model) {
        Customer customer = customerManager.findCustomerById(id);
        model.addAttribute("customer", customer);
        return "page-edit-customer";
    }

    @PostMapping("/page-edit-customer/{id}")
    public String updateCustomer(@PathVariable("id") int id, @ModelAttribute("customer") Customer customer) {
        customerManager.updateCustomer(customer);
        return "redirect:/page-list-customers";
    }

}
