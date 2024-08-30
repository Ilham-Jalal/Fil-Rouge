package com.example.demo.services;

import com.example.demo.Exeption.UserNotFoundExeption;
import com.example.demo.dto.SignUpRequest;
import com.example.demo.enums.Role;
import com.example.demo.models.*;
import com.example.demo.repositorys.AdminRepository;
import com.example.demo.repositorys.LivreurRepository;
import com.example.demo.repositorys.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final AdminRepository adminRepository;
    private final LivreurRepository livreurRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(AdminRepository adminRepository, LivreurRepository livreurRepository, UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.livreurRepository = livreurRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Utilisateur> findAllUsers() {
        return utilisateurRepository.findAll();
    }

    public User findUserById(Long id) {
        return utilisateurRepository.findById(id).orElseThrow(() -> new UserNotFoundExeption("User not found"));
    }

    public Optional<Utilisateur> findUtilisateurByUsername(String username) {
        return utilisateurRepository.findByUsername(username);
    }


    public Optional<Livreur> findLivreurByUsername(String username) {
        return livreurRepository.findByUsername(username);
    }

    public Optional<Admin> findAdminByUsername(String username) {
        return adminRepository.findByUsername(username);
    }


    public User signUpUser(SignUpRequest signUpRequest) {
        String hashedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        Utilisateur user = new Utilisateur();
        user.setRole(Role.USER);
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(hashedPassword);

        return utilisateurRepository.save(user);
    }

    public User addUserByAdmin(Role role, SignUpRequest signUpRequest) {
        String hashedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        User user;
        switch (role) {
            case ADMIN:
                Admin admin = new Admin();
                admin.setRole(Role.ADMIN);
                admin.setUsername(signUpRequest.getUsername());
                admin.setEmail(signUpRequest.getEmail());
                admin.setPassword(hashedPassword);
                user = adminRepository.save(admin);
                break;
            case LIVREUR:
                Livreur livreur = new Livreur();
                livreur.setRole(Role.LIVREUR);
                livreur.setUsername(signUpRequest.getUsername());
                livreur.setEmail(signUpRequest.getEmail());
                livreur.setPassword(hashedPassword);
                livreur.setDeliveryZone(signUpRequest.getDeliveryZone());
                user = livreurRepository.save(livreur);
                break;
            default:
                throw new IllegalArgumentException("Invalid role for admin");
        }

        return user;
    }
}

