package com.example.demo.services;

import com.example.demo.Exeption.UserNotFoundExeption;
import com.example.demo.dto.SignUpRequest;
import com.example.demo.dto.UserDTO;
import com.example.demo.enums.Role;
import com.example.demo.models.*;
import com.example.demo.repositorys.AdminRepository;
import com.example.demo.repositorys.LivreurRepository;
import com.example.demo.repositorys.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private LivreurRepository livreurRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllUsers() {
        List<Utilisateur> utilisateurs = List.of(new Utilisateur(), new Utilisateur());
        when(utilisateurRepository.findAll()).thenReturn(utilisateurs);

        List<Utilisateur> result = userService.findAllUsers();

        assertEquals(2, result.size());
        verify(utilisateurRepository, times(1)).findAll();
    }

    @Test
    void testFindUserById() {
        Utilisateur user = new Utilisateur();
        user.setId(1L);
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(utilisateurRepository, times(1)).findById(1L);
    }

    @Test
    void testFindUserByIdUserNotFound() {
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundExeption.class, () -> userService.findUserById(1L));
    }

    @Test
    void testSignUpUser() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("testuser");
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setPassword("password");

        String hashedPassword = "hashedpassword";
        when(passwordEncoder.encode("password")).thenReturn(hashedPassword);

        Utilisateur savedUser = new Utilisateur();
        savedUser.setUsername("testuser");
        savedUser.setEmail("test@example.com");
        savedUser.setPassword(hashedPassword);
        savedUser.setRole(Role.USER);

        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(savedUser);

        User result = userService.signUpUser(signUpRequest);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals(hashedPassword, result.getPassword());
        verify(utilisateurRepository, times(1)).save(any(Utilisateur.class));
    }

    @Test
    void testAddUserByAdminAdminRole() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("adminuser");
        userDTO.setEmail("admin@example.com");
        userDTO.setPassword("password");

        String hashedPassword = "hashedpassword";
        when(passwordEncoder.encode("password")).thenReturn(hashedPassword);

        Admin admin = new Admin();
        admin.setUsername("adminuser");
        admin.setEmail("admin@example.com");
        admin.setPassword(hashedPassword);
        admin.setRole(Role.ADMIN);

        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        User result = userService.addUserByAdmin(Role.ADMIN, userDTO);

        assertNotNull(result);
        assertEquals("adminuser", result.getUsername());
        assertEquals(Role.ADMIN, result.getRole());
        verify(adminRepository, times(1)).save(any(Admin.class));
    }

    @Test
    void testAddUserByAdminLivreurRole() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("livreuruser");
        userDTO.setEmail("livreur@example.com");
        userDTO.setPassword("password");

        String hashedPassword = "hashedpassword";
        when(passwordEncoder.encode("password")).thenReturn(hashedPassword);

        Livreur livreur = new Livreur();
        livreur.setUsername("livreuruser");
        livreur.setEmail("livreur@example.com");
        livreur.setPassword(hashedPassword);
        livreur.setRole(Role.LIVREUR);

        when(livreurRepository.save(any(Livreur.class))).thenReturn(livreur);

        // When
        User result = userService.addUserByAdmin(Role.LIVREUR, userDTO);

        // Then
        assertNotNull(result);
        assertEquals("livreuruser", result.getUsername());
        assertEquals(Role.LIVREUR, result.getRole());
        verify(livreurRepository, times(1)).save(any(Livreur.class));
    }

    @Test
    void testAddUserByAdminInvalidRole() {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("invaliduser");

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> userService.addUserByAdmin(Role.USER, userDTO));
    }

    @Test
    void testGetHistoriqueVentes() {
        // Given
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);
        List<Annonce> ventes = List.of(new Annonce(), new Annonce());
        utilisateur.setVentes(ventes);

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(utilisateur));

        // When
        List<Annonce> result = userService.getHistoriqueVentes(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(utilisateurRepository, times(1)).findById(1L);
    }

    @Test
    void testGetHistoriqueAchats() {
        // Given
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);
        List<Annonce> achats = List.of(new Annonce(), new Annonce());
        utilisateur.setAchats(achats);

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(utilisateur));

        // When
        List<Annonce> result = userService.getHistoriqueAchats(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(utilisateurRepository, times(1)).findById(1L);
    }
}
