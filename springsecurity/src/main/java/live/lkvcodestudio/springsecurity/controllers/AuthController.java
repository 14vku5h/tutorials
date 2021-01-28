package live.lkvcodestudio.springsecurity.controllers;

import live.lkvcodestudio.springsecurity.models.User;
import live.lkvcodestudio.springsecurity.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping(value = {"/", "/login"})
    public String login(Model model, String error, String logout, Principal principal) {
        if (principal != null)
            return "redirect:/profile";
        if (error != null)
            model.addAttribute("error", error);

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @GetMapping("/register")
    public String register(Model model, Principal principal) {
        if (principal != null) {
            return "redirect:/profile";
        }
        return "register";
    }

    @PostMapping("/register")
    public ModelAndView register(User user) {
        ModelAndView mv = userService.registerUser(user);
        return mv;
    }

    /* to check the authentication principal details */
    @GetMapping("/principal")
    @ResponseBody
    public Principal principal(Principal principal) {
        return principal;
    }

    /* you can only open this page if user is authenticated, why? see SecurityConfig.java */
    @GetMapping("/profile")
    public String profile(Model model) {
        /* simply open the profile page
         *  and display the name, email, mobile number and profile picture
         *  through authentication principal, how? see profile.html */
        return "/profile";
    }
}