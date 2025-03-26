package sg.edu.nus.iss.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AngularRouteController {
    
    @RequestMapping({
        "/",
        "/recipe-preview/**",
        "/partners/**",
        "/login/**",
        "/activity/**",
        "/recipes/**",
        "/nutrition/**",
        "/clear-my-fridge/**",
        "/account/**",
        "/users/**"
    })
    public String forwardToAngularRoute() {
        return "forward:/index.html";
    }

}
