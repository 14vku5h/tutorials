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
        ModelAndView mv = new ModelAndView("profile");
        Optional<User> checkUser = userRepository.findByEmail(user.getEmail());
        if(!checkUser.isPresent()){
            checkUser = userRepository.findByMobile(user.getMobile());
            if(!checkUser.isPresent()) {
                String plainPassowrd = user.getPassword();
                user.setPassword(passwordEncoder.encode(plainPassowrd));
                //setting the default role as string
                user.setRole("ROLE_USER");
                /* role based authorization will be demonstrated in part 2 of this series
                 keep visiting easytutorials.live */
                user = userRepository.save(user);

                /* auto login first time */
                securityService.loginFirstTime(user,plainPassowrd);
            }else{
                mv.setViewName("register");
                mv.addObject("error","A user is already present with this mobile number");
            }
        }else{
            mv.setViewName("register");
            mv.addObject("error","User already registered with this email");
        }
        return mv;
    }


/* This method will use to change the authentication principal details without logout */
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

    /* if you need something with logged in user principal details */
    public CustomUserPrincipal getLoggedInUserPrincipal(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserPrincipal user = (CustomUserPrincipal) auth.getPrincipal();
        return user;
    }
    /* if you need all details of logged in user */
    public User getLoggedInUser(){
        return userRepository.findByEmail(getLoggedInUserPrincipal().getEmail()).get();
    }

}
