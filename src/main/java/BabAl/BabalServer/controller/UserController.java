package BabAl.BabalServer.controller;

import BabAl.BabalServer.apiPayload.ApiResponse;
import BabAl.BabalServer.dto.request.LoginDto;
import BabAl.BabalServer.dto.request.SignInDto;
import BabAl.BabalServer.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signin")
    public ApiResponse<Long> signIn(@Valid @RequestBody SignInDto dto) throws Exception {
        return ApiResponse.onSuccess(userService.signIn(dto));
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody LoginDto dto) throws Exception {
        return ApiResponse.onSuccess(userService.login(dto));
    }
}
