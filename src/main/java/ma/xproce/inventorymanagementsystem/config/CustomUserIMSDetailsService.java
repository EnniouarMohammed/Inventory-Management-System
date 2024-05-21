package ma.xproce.inventorymanagementsystem.config;

import ma.xproce.inventorymanagementsystem.dao.entities.UserIMS;
import ma.xproce.inventorymanagementsystem.dao.repositories.UserIMSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserIMSDetailsService implements UserDetailsService {
    @Autowired
    private UserIMSRepository userIMSRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserIMS user = userIMSRepository.findUserIMSByEmail(email);
        if(user != null) {
            return User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .build();
        } else {
            throw new UsernameNotFoundException("Invalid email or password");
        }
    }
}