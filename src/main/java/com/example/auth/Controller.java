package com.example.auth;

import com.example.auth.dto.ResponseUserDto;
import com.example.auth.model.Users;
import com.example.auth.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class Controller {
    private final UsersService usersService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseUserDto> createUser(@RequestBody Users user) {
        Optional<Users> newUser = usersService.save(user);
        if (newUser.isPresent()) {
            ResponseUserDto userDto = ResponseUserDto.builder()
                    .userName(newUser.get().getUserName())
                    .name(newUser.get().getName())
                    .surName(newUser.get().getSurName())
                    .password(newUser.get().getPassword()).build();

            return new ResponseEntity<>(userDto, HttpStatus.CREATED);
        }
        // return new ResponseEntity<>(null,HttpStatusCode.valueOf(400));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Users user) {
        Users users = usersService.login(user);

        if (users != null) {

            return new ResponseEntity<>(users.getToken(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatusCode.valueOf(401));
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseUserDto> tokenUser(@RequestParam String token) {
        ResponseUserDto userDto = usersService.verifyUser(token);

        if (userDto != null) {
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}







