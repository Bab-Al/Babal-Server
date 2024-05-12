package BabAl.BabalServer.controller;

import BabAl.BabalServer.apiPayload.ApiResponse;
import BabAl.BabalServer.dto.response.SettingProfileResponseDto;
import BabAl.BabalServer.dto.response.SettingResponseDto;
import BabAl.BabalServer.jwt.JwtUtil;
import BabAl.BabalServer.service.SettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
