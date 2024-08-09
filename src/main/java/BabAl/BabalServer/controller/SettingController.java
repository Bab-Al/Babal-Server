package BabAl.BabalServer.controller;

import BabAl.BabalServer.apiPayload.ApiResponse;
import BabAl.BabalServer.apiPayload.code.status.SuccessStatus;
import BabAl.BabalServer.dto.request.SettingFoodCategoryDto;
import BabAl.BabalServer.dto.request.SettingPasswordDto;
import BabAl.BabalServer.dto.request.SettingProfileDto;
import BabAl.BabalServer.dto.response.SettingFoodCategoryResponseDto;
import BabAl.BabalServer.dto.response.SettingProfileResponseDto;
import BabAl.BabalServer.dto.response.SettingResponseDto;
import BabAl.BabalServer.jwt.JwtUtil;
import BabAl.BabalServer.service.SettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/setting")
@Tag(name = "My Page", description = "My Page Setting API")
public class SettingController {

    private final SettingService settingService;

    @GetMapping()
    @Operation(summary = "마이페이지 조회", description = "마이페이지 조회 시 사용하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청이 정상 처리되었습니다", content = @Content(mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다", content = @Content(mediaType = "application/json"))
    })
    public ApiResponse<SettingResponseDto> getSetting(@RequestHeader("Authorization") String token) {
        String userEmail = JwtUtil.getEmail(token.substring(7));
        return ApiResponse.onSuccess(settingService.getSetting(userEmail));
    }

    @GetMapping("/profile")
    @Operation(summary = "마이페이지 Edit Profile 조회", description = "마이페이지에서 Profile 조회 시 사용하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청이 정상 처리되었습니다", content = @Content(mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다", content = @Content(mediaType = "application/json"))
    })
    public ApiResponse<SettingProfileResponseDto> getSettingProfile(@RequestHeader("Authorization") String token) {
        String userEmail = JwtUtil.getEmail(token.substring(7));
        return ApiResponse.onSuccess(settingService.getSettingProfile(userEmail));
    }

    @PostMapping("/profile")
    @Operation(summary = "마이페이지 Edit Profile 수정", description = "마이페이지에서 Profile 수정 시 사용하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청이 정상 처리되었습니다", content = @Content(mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다", content = @Content(mediaType = "application/json"))
    })
    public ApiResponse<SuccessStatus> setSettingProfile(@RequestHeader("Authorization") String token,
                                                        @Valid @RequestBody SettingProfileDto dto) {
        String userEmail = JwtUtil.getEmail(token.substring(7));
        return ApiResponse.onSuccess(settingService.setSettingProfile(userEmail, dto));
    }

    @PostMapping("/profile/new-pw")
    @Operation(summary = "마이페이지에서 비밀번호 수정", description = "마이페이지에서 비밀번호 수정 시 사용하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청이 정상 처리되었습니다", content = @Content(mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다", content = @Content(mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4004", description = "기존 비밀번호가 아닙니다", content = @Content(mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4005", description = "새 비밀번호와 확인 비밀번호가 맞지 않습니다", content = @Content(mediaType = "application/json"))
    })
    public ApiResponse<SuccessStatus> setSettingPassword(@RequestHeader("Authorization") String token,
                                                         @Valid @RequestBody SettingPasswordDto dto) {
        String userEmail = JwtUtil.getEmail(token.substring(7));
        return ApiResponse.onSuccess(settingService.setSettingPassword(userEmail, dto));
    }

    @GetMapping("/foodCategory")
    @Operation(summary = "마이페이지 Edit Food Category 조회", description = "마이페이지에서 선호하는 Food Category 조회 시 사용하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청이 정상 처리되었습니다", content = @Content(mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다", content = @Content(mediaType = "application/json"))
    })
    public ApiResponse<SettingFoodCategoryResponseDto> getSettingFoodCategory(@RequestHeader("Authorization") String token) {
        String userEmail = JwtUtil.getEmail(token.substring(7));
        return ApiResponse.onSuccess(settingService.getSettingFoodCategory(userEmail));
    }

    @PostMapping("/foodCategory")
    @Operation(summary = "마이페이지 Edit Food Category 수정", description = "마이페이지에서 선호하는 Food Category 수정 시 사용하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청이 정상 처리되었습니다", content = @Content(mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다", content = @Content(mediaType = "application/json"))
    })
    public ApiResponse<SuccessStatus> setSettingFoodCategory(@RequestHeader("Authorization") String token,
                                                             @Valid @RequestBody SettingFoodCategoryDto dto) {
        String userEmail = JwtUtil.getEmail(token.substring(7));
        return ApiResponse.onSuccess(settingService.setSettingFoodCategory(userEmail, dto));
    }

}
