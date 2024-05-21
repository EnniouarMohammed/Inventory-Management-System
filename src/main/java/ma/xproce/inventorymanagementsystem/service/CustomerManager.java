package ma.xproce.inventorymanagementsystem.service;

import ma.xproce.inventorymanagementsystem.dao.entities.Customer;
import ma.xproce.inventorymanagementsystem.dao.entities.UserIMS;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CustomerManager {
    Customer addCustomer(Customer customer);

    Customer updateCustomer(Customer customer);

    boolean deleteCustomerById(Integer id);

    Customer findCustomerById(int id);

    List<Customer> getAllCustomers();

    Page<Customer> searchCustomers(String keyword, UserIMS user, int page, int size);

}
