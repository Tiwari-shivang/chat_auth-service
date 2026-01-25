package com.chat.auth.services;

import com.chat.auth.repositories.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class SecurityService implements UserDetailsService {
    @Autowired
    private userRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email){
        return userRepository.findByEmail(email);
    }
}
