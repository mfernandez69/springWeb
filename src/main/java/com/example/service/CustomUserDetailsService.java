package com.example.service;

import com.example.models.Administrador;
import com.example.repository.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private AdministradorRepository administradorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Administrador admin = administradorRepository.findByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        return new User(admin.getUsername(), admin.getPassword(), new ArrayList<>());
    }
}