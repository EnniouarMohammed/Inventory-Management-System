package ma.xproce.inventorymanagementsystem;

import ma.xproce.inventorymanagementsystem.dao.entities.Product;
import ma.xproce.inventorymanagementsystem.service.ProductManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class InventoryManagementSystemApplication {
	@Autowired
	private ProductManager productManager;

	public static void main(String[] args) {
		SpringApplication.run(InventoryManagementSystemApplication.class, args);
	}

}