package ma.xproce.inventorymanagementsystem.service;

import ma.xproce.inventorymanagementsystem.dto.RegistrationDto;
import ma.xproce.inventorymanagementsystem.dao.entities.UserIMS;
import ma.xproce.inventorymanagementsystem.dao.repositories.UserIMSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserIMSManagerService implements UserIMSManager{
    @Autowired
    private UserIMSRepository userIMSRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public void addUser(RegistrationDto registrationDto) {
        UserIMS user = new UserIMS();
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setPhoneNumber(registrationDto.getPhoneNumber());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        userIMSRepository.save(user);
    }

    @Override
    public UserIMS findUserIMSByEmail(String email) {
        try{
            return userIMSRepository.findUserIMSByEmail(email);
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            return null;
        }
    }
}
