package com.example.demo.implimentation;

import com.example.demo.models.User;
import com.example.demo.repositorys.AdminRepository;
import com.example.demo.repositorys.LivreurRepository;
import com.example.demo.repositorys.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private LivreurRepository livreurRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = utilisateurRepository.findByUsername(username)
                .orElseGet(() -> adminRepository.findByUsername(username)
                        .orElseGet(() -> livreurRepository.findByUsername(username)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"))));

        return user;
    }
}

