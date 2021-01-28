package live.lkvcodestudio.springsecurity.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import live.lkvcodestudio.springsecurity.models.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class CustomUserPrincipal implements UserDetails, Serializable {

    private String  uniqueId;
    private String name;
    private String email;
    private String mobile;
    private String profilePic;

    @JsonIgnore
    private String username;

    @JsonIgnore
    private String password;
    @JsonIgnore
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserPrincipal(String uniqueId, String loggedInName, String profilePic, String username, String email, String mobile, String password, Collection<? extends GrantedAuthority> authorities) {
        this.uniqueId = uniqueId;
        this.name =loggedInName;
        this.email=email;
        this.mobile=mobile;
        this.profilePic=profilePic;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static CustomUserPrincipal createWithEmail(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
        return new CustomUserPrincipal(
                user.getUniqueId(),
                user.getName(),
                user.getProfilePic(),
                user.getEmail(),
                user.getEmail(),
                user.getMobile(),
                user.getPassword(),
                authorities
        );
    }
    public static CustomUserPrincipal createWithMobile(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
        return new CustomUserPrincipal(
                user.getUniqueId(),
                user.getName(),
                user.getProfilePic(),
                user.getMobile(),
                user.getEmail(),
                user.getMobile(),
                user.getPassword(),
                authorities
        );
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}