package live.lkvcodestudio.springsecurity.services;

import live.lkvcodestudio.springsecurity.models.User;
import live.lkvcodestudio.springsecurity.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private SecurityService securityService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public ModelAndView registerUser(User user) {
        ModelAndView mv = new ModelAndView("admin/profile");
        Optional<User> checkUser = userRepository.findByEmail(user.getEmail());
        if(!checkUser.isPresent()){
            checkUser = userRepository.findByMobile(user.getMobile());
            if(!checkUser.isPresent()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                user.setRole("ROLE_USER");
                user = userRepository.save(user);
                securityService.loginFirstTime(user);
            }else{
                mv.setViewName("register");
                mv.addObject("error","A user is already present with this mobile number");
            }
        }else{
            mv.setViewName("register");
            mv.addObject("error","User already registered with this email");
        }
        mv.setViewName("admin/profile");
        return mv;
    }



    public void updateAuthenticationDetails(User updatedUser) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserPrincipal user = (CustomUserPrincipal) auth.getPrincipal();
        user.setName(updatedUser.getName());
        user.setProfilePic(updatedUser.getProfilePic());
        user.setEmail(updatedUser.getEmail());
        user.setMobile(updatedUser.getMobile());
        Authentication newAuth = new UsernamePasswordAuthenticationToken(user, auth.getCredentials(), auth.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    public CustomUserPrincipal getLoggedInUserPrincipal(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserPrincipal user = (CustomUserPrincipal) auth.getPrincipal();
        return user;
    }

    public User getLoggedInUser(){
        return userRepository.findByEmail(getLoggedInUserPrincipal().getEmail()).get();
    }

}
