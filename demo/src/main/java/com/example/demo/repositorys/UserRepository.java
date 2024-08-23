package com.example.demo.repositorys;

import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
//    @Query("SELECT u.id FROM User u WHERE u.username = :username")
//    Integer findIdByUsername(@Param("username") String username);

}
