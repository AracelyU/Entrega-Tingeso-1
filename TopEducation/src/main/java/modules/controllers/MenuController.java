package modules.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

    @GetMapping("/TopEducationWeb")
    public String menuPrincipal() {
        return "menu";
    }
}
