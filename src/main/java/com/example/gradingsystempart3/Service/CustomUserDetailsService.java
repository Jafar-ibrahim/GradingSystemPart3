package com.example.gradingsystempart3.Service;

import com.example.gradingsystempart3.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;
    @Autowired
    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userService.getByUsername(username);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("User is not found");
        }
        String password = user.get().getPassword();
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(String.valueOf(user.get().getRole()));
        return new org.springframework.security.core.userdetails.User(username,password,authorities);
    }
}
