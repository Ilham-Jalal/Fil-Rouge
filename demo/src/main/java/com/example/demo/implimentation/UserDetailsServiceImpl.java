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
                .map(utilisateur -> (User) utilisateur)
                .orElseGet(() -> adminRepository.findByUsername(username)
                        .map(admin -> (User) admin)
                        .orElseGet(() -> livreurRepository.findByUsername(username)
                                .map(livreur -> (User) livreur)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"))));
        return user;
    }
}
