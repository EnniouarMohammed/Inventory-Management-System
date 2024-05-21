package ma.xproce.inventorymanagementsystem.service;

import ma.xproce.inventorymanagementsystem.config.SecurityUtil;
import ma.xproce.inventorymanagementsystem.dao.entities.Category;
import ma.xproce.inventorymanagementsystem.dao.entities.Customer;
import ma.xproce.inventorymanagementsystem.dao.entities.UserIMS;
import ma.xproce.inventorymanagementsystem.dao.repositories.CustomerRepository;
import ma.xproce.inventorymanagementsystem.dao.repositories.UserIMSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerManagerService implements CustomerManager{
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserIMSRepository userIMSRepository;

    private UserIMS getCurrentUser() {
        String email = SecurityUtil.getSessionUser();
        return userIMSRepository.findUserIMSByEmail(email);
    }

    @Override
    public Customer addCustomer(Customer customer) {
        try{
            UserIMS user = getCurrentUser();
            customer.setCreatedBy(user);
            return customerRepository.save(customer);
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            return null;
        }
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        try{
            UserIMS user = getCurrentUser();
            if (customer.getCreatedBy().equals(user)){
                return customerRepository.save(customer);
            }
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            return null;
        }
        return null;
    }

    @Override
    public boolean deleteCustomerById(Integer id) {
        try {
            UserIMS user = getCurrentUser();
            Customer customer = customerRepository.findById(id).orElse(null);
            if (customer != null && customer.getCreatedBy().equals(user)) {
                customerRepository.deleteById(id);
                return true;
            }
        } catch (Exception exception) {
            return false;
        }
        return false;
    }

    @Override
    public Customer findCustomerById(int id) {
        try{
            UserIMS user = getCurrentUser();
            Customer customer = customerRepository.findById(id).orElse(null);
            if (customer != null && customer.getCreatedBy().equals(user)) {
                return customer;
            }
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            return null;
        }
        return null;
    }

    @Override
    public List<Customer> getAllCustomers() {
        try{
            UserIMS user = getCurrentUser();
            return customerRepository.findByCreatedBy(user);
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            return null;
        }
    }

    @Override
    public Page<Customer> searchCustomers(String keyword,UserIMS user, int page, int taille) {
        return customerRepository.findByFullNameContainsAndCreatedBy(keyword, user, PageRequest.of(page, taille));
    }

}
