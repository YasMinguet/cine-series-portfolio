package com.cinescope.cineseries.service.impl;

import com.cinescope.cineseries.entity.AppUser;
import com.cinescope.cineseries.repository.AppUserRepository;
import com.cinescope.cineseries.util.Constantes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AppUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(Constantes.ERROR_USER_NOT_FOUND));

        var authorities = org.springframework.security.core.authority.AuthorityUtils
                .commaSeparatedStringToAuthorityList(user.getRole()); // expects "ROLE_USER" or "ROLE_ADMIN"

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
