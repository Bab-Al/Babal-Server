package BabAl.BabalServer.controller;

import BabAl.BabalServer.dto.request.LoginDto;
import BabAl.BabalServer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto dto) {
        return ResponseEntity.ok().body(userService.login(dto.getEmail(), dto.getPassword()));
    }
}
