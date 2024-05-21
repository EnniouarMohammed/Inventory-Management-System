package ma.xproce.inventorymanagementsystem.web;

import jakarta.servlet.http.HttpSession;
import ma.xproce.inventorymanagementsystem.config.SecurityUtil;
import ma.xproce.inventorymanagementsystem.dao.entities.Product;
import ma.xproce.inventorymanagementsystem.dao.entities.UserIMS;
import ma.xproce.inventorymanagementsystem.dao.repositories.*;
import ma.xproce.inventorymanagementsystem.service.ProductManager;
import ma.xproce.inventorymanagementsystem.service.UserIMSManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private ProductManager productManager;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    UserIMSManager userIMSManager;


    /*******************************************************Home*/
    @GetMapping("")
    public String Home2(Model model) {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String Home(Model model, HttpSession session) {
        String username = SecurityUtil.getSessionUser();
        UserIMS user = userIMSManager.findUserIMSByEmail(username);

        session.setAttribute("email", user.getEmail());
        session.setAttribute("firstName", user.getFirstName());
        session.setAttribute("lastName", user.getLastName());

        /*Sales*/
        int totalCompletedSales = saleRepository.totalCompletedSales(user);
        model.addAttribute("totalCompletedSales", totalCompletedSales);

        int totalPendingSales = saleRepository.totalPendingSales(user);
        model.addAttribute("totalPendingSales", totalPendingSales);

        double totalSalesRevenue = saleRepository.totalSalesRevenue(user);
        model.addAttribute("totalSalesRevenue",totalSalesRevenue);

        /*Products*/
        int totalProducts = productRepository.totalProducts();
        model.addAttribute("totalProducts",totalProducts);

        /**********************************************************************Top Selling Products*/
        List<Integer> top5ProductIds = saleRepository.findTop5SellingProductIds(PageRequest.of(0, 5));
        List<Product> top5Products = new ArrayList<>();
        for (Integer productId : top5ProductIds) {
            if (productId != 0){
                Product product = productManager.findProductById(productId);
                if (product != null) {
                    top5Products.add(product);
                }
            }
        }
        model.addAttribute("top5Products", top5Products);



        return "index";
    }

    @GetMapping("/income")
    @ResponseBody
    public Map<String, Double> getIncome(@RequestParam String period) {
        String username = SecurityUtil.getSessionUser();
        UserIMS user = userIMSManager.findUserIMSByEmail(username);

        LocalDate today = LocalDate.now();
        double income = 0;

        switch (period.toLowerCase()) {
            case "week":
                LocalDate startOfWeek = today.with(java.time.DayOfWeek.MONDAY);
                income = saleRepository.calculateWeeklyIncome(startOfWeek,user);
                break;
            case "month":
                LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
                income = saleRepository.calculateMonthlyIncome(startOfMonth,user);
                break;
            case "year":
                LocalDate startOfYear = today.with(TemporalAdjusters.firstDayOfYear());
                income = saleRepository.calculateYearlyIncome(startOfYear,user);
                break;
        }

        Map<String, Double> response = new HashMap<>();
        response.put("income", income);

        return response;
    }

    @GetMapping("/expenses")
    @ResponseBody
    public Map<String, Double> getExpenses(@RequestParam String period) {
        String username = SecurityUtil.getSessionUser();
        UserIMS user = userIMSManager.findUserIMSByEmail(username);

        LocalDate today = LocalDate.now();
        double expenses = 0;

        switch (period.toLowerCase()) {
            case "week":
                LocalDate startOfWeek = today.with(java.time.DayOfWeek.MONDAY);
                expenses = saleRepository.calculateWeeklyExpenses(startOfWeek,user);
                break;
            case "month":
                LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
                expenses = saleRepository.calculateMonthlyExpenses(startOfMonth,user);
                break;
            case "year":
                LocalDate startOfYear = today.with(TemporalAdjusters.firstDayOfYear());
                expenses = saleRepository.calculateYearlyExpenses(startOfYear,user);
                break;
        }

        Map<String, Double> response = new HashMap<>();
        response.put("expenses", expenses);

        return response;
    }


}
