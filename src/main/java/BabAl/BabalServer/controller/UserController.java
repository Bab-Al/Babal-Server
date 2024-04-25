package BabAl.BabalServer.controller;

import BabAl.BabalServer.apiPayload.ApiResponse;
import BabAl.BabalServer.apiPayload.code.status.SuccessStatus;
import BabAl.BabalServer.dto.request.LoginDto;
import BabAl.BabalServer.dto.request.SignInDto;
import BabAl.BabalServer.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "USER", description = "USER SignIn/Login API")
public class UserController {

    private final UserService userService;

    @PostMapping("/signin")
    @Operation(summary = "회원가입", description = "회원가입할 때 사용하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원가입 요청이 정상 처리되었습니다", content = @Content(mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4003", description = "이미 등록된 사용자입니다", content = @Content(mediaType = "application/json"))
    })
    public ApiResponse<SuccessStatus> signIn(@Valid @RequestBody SignInDto dto) throws Exception {
        return ApiResponse.onSuccess(userService.signIn(dto));
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody LoginDto dto) throws Exception {
        return ApiResponse.onSuccess(userService.login(dto));
    }
}
