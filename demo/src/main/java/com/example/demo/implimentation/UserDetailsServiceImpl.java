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

    private final UtilisateurRepository utilisateurRepository;

    private final AdminRepository adminRepository;

    private final LivreurRepository livreurRepository;

    public UserDetailsServiceImpl(UtilisateurRepository utilisateurRepository, AdminRepository adminRepository, LivreurRepository livreurRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.adminRepository = adminRepository;
        this.livreurRepository = livreurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return utilisateurRepository.findByUsername(username)
                .map(User.class::cast)
                .orElseGet(() -> adminRepository.findByUsername(username)
                        .map(User.class::cast)
                        .orElseGet(() -> livreurRepository.findByUsername(username)
                                .map(User.class::cast)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"))));
    }
}
