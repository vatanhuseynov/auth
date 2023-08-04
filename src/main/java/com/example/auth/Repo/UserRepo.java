package com.example.auth.Repo;

import com.example.auth.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepo extends JpaRepository<Users,Long> {
      Optional<Users> findUsersByToken(String token);
      Optional<Users> findUsersByUserName(String userName);
      Users save(Users user);
}
