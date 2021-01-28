package live.lkvcodestudio.springsecurity.services;


import live.lkvcodestudio.springsecurity.models.User;
import live.lkvcodestudio.springsecurity.repositories.UserRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;



@Service
@Log
public class SecurityService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Let people login with either email or mobile
        CustomUserPrincipal userPrincipal = null;
        Optional<User> user = null;

        /* check user if user enters email as username*/
        user = userRepository.findByEmail(username);
        if (user.isPresent())
            userPrincipal = CustomUserPrincipal.createWithEmail(user.get());
        else {
            /* check user if user not present with email, username may be mobile no.*/
            user = userRepository.findByMobile(username);
            if (user.isPresent())
                userPrincipal = CustomUserPrincipal.createWithMobile(user.get());
        }
        /* check that user is present with email or mobile */
        if (userPrincipal == null)
            throw new UsernameNotFoundException("User not found with username: " + username);
        else
            return userPrincipal;
    }

/* Username Password Authentication method to allow user to log in just after registration*/
    public void loginFirstTime(User user,String plainPassowrd) {
        log.info("Auto login .....!");
        UserDetails userDetails = loadUserByUsername(user.getEmail());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, plainPassowrd, userDetails.getAuthorities());
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            log.info(String.format("Auto login %s successfully!", user.getEmail()));
        } else System.err.println("auto login failed");
    }

}
