package ma.xproce.inventorymanagementsystem.web;

import jakarta.validation.Valid;
import ma.xproce.inventorymanagementsystem.dto.RegistrationDto;
import ma.xproce.inventorymanagementsystem.dao.entities.UserIMS;
import ma.xproce.inventorymanagementsystem.service.UserIMSManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    @Autowired
    UserIMSManager userIMSManager;

    /**********Register************/
    @GetMapping("/auth-sign-up")
    public String getRegisterForm(Model model) {
        RegistrationDto user = new RegistrationDto();
        model.addAttribute("user", user);
        return "auth-sign-up";
    }

    @PostMapping("/auth-sign-up")
    public String register(@Valid @ModelAttribute("user")RegistrationDto user, Model model, BindingResult bindingResult) {

        UserIMS existingUserEmail = userIMSManager.findUserIMSByEmail(user.getEmail());
        if (existingUserEmail != null && existingUserEmail.getEmail() != null && !existingUserEmail.getEmail().isEmpty()){
            return "redirect:/auth-sign-up?alreadyExists";
        }

        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.toString());
            return "auth-sign-up";
        }

        userIMSManager.addUser(user);
        return "redirect:/auth-sign-in?success";
    }

    /************Login************/
    @GetMapping("/auth-sign-in")
    public String loginPage() {
        return "auth-sign-in";
    }
}
