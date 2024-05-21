package ma.xproce.inventorymanagementsystem.web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class errorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            return "page-error";
        } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return "page-error-500";
        } else {
            return "page-error";
        }
    }
}