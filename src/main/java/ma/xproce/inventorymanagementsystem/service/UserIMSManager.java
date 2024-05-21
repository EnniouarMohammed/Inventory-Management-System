package ma.xproce.inventorymanagementsystem.service;

import ma.xproce.inventorymanagementsystem.dto.RegistrationDto;
import ma.xproce.inventorymanagementsystem.dao.entities.UserIMS;
import org.springframework.stereotype.Component;

@Component
public interface UserIMSManager {
    void addUser(RegistrationDto registrationDto);

    UserIMS findUserIMSByEmail(String email);
}
