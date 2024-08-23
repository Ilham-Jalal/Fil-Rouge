package com.example.demo.services;

import com.example.demo.Exeption.UserNotFoundExeption;
import com.example.demo.dto.SignUpRequest;
import com.example.demo.enums.Role;
import com.example.demo.models.*;
import com.example.demo.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAllUsers(){return userRepository.findAll();}
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundExeption("User not found"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User signUpUser(SignUpRequest signUpRequest) {
        String hashedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        Utilisateur user = new Utilisateur();
        user.setRole(Role.USER);
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }
    public User addUserByAdmin(Role role, SignUpRequest signUpRequest) {
        String hashedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        User user;
        switch (role) {
            case ADMIN:
                user = new Admin();
                user.setRole(Role.ADMIN);
                break;
            case LIVREUR:
                user = new Livreur();
                user.setRole(Role.LIVREUR);
                ((Livreur) user).setDeliveryZone(signUpRequest.getDeliveryZone());  // Exemple de champ spécifique à Livreur
                break;
            default:
                throw new IllegalArgumentException("Invalid role for admin");
        }

        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }
//
//    public List<User> getTechnicians() {
//        return userRepository.findAll()
//                .stream()
//                .filter(user -> user instanceof TechnicianIT)
//                .collect(Collectors.toList());
//    }
//
//    public Integer findIdUserByUsername(String username){
//        return  userRepository.findIdByUsername(username);
//    }
}
