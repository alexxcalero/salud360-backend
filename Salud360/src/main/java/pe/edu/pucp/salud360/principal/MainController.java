package pe.edu.pucp.salud360.principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @RequestMapping("/")
    public String home() {
        return "Bienvenido a Salud 360";
    }
}
