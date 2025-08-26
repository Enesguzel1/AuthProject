package com.enesguzel.authify.service.impl;

import com.enesguzel.authify.entity.UserEntity;
import com.enesguzel.authify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AppUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity existingUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new User(existingUser.getEmail(),existingUser.getPassword(),new ArrayList<>());
    }
}
