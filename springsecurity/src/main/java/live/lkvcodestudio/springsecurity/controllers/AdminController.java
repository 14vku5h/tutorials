package live.lkvcodestudio.springsecurity.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping("/profile")
    public String profile(Model model){
        return "admin/profile";
    }
}

