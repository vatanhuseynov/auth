package com.example.auth.service;

import com.example.auth.Repo.UserRepo;
import com.example.auth.dto.ResponseUserDto;
import com.example.auth.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UserRepo userRepo;

    public Optional<Users> save(Users user) {
        Optional<Users> existUser = userRepo.findUsersByUserName(user.getUserName());
        if (existUser.isEmpty()) {
            return Optional.of(userRepo.save(user));
        } else {
            return Optional.empty();
        }
    }

    public Users login(Users user) {
        Optional<Users> existUser = userRepo.findUsersByUserName(user.getUserName());
        if (existUser.isPresent() && existUser.get().getPassword().equals(user.getPassword())) {
            existUser.get().setToken(UUID.randomUUID().toString());
            userRepo.save(existUser.get());
            return existUser.get();
        }
        return null;
    }

    public ResponseUserDto verifyUser(String token) {
        Optional<Users> existUser = userRepo.findUsersByToken(token);
        if (existUser.isPresent()) {
            return ResponseUserDto.builder()
                    .userName(existUser.get().getUserName())
                    .name(existUser.get().getName())
                    .surName(existUser.get().getSurName()).build();
        }
        return null;
    }

}
