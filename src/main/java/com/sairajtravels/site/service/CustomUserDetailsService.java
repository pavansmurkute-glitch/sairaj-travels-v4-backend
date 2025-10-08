package com.sairajtravels.site.service;

import com.sairajtravels.site.entity.AdminUser;
import com.sairajtravels.site.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser adminUser = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if (!adminUser.getIsActive()) {
            throw new UsernameNotFoundException("User account is disabled: " + username);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // Add role-based authorities
        if (adminUser.getRole() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + adminUser.getRole().getRoleName()));
        }

        return new User(
                adminUser.getUsername(),
                adminUser.getPassword(),
                adminUser.getIsActive(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                !adminUser.getMustChangePassword(), // accountNonLocked
                authorities
        );
    }
}
