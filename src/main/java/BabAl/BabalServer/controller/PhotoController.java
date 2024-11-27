package BabAl.BabalServer.controller;

import BabAl.BabalServer.apiPayload.ApiResponse;
import BabAl.BabalServer.apiPayload.code.status.ErrorStatus;
import BabAl.BabalServer.apiPayload.code.status.SuccessStatus;
import BabAl.BabalServer.apiPayload.exception.GeneralException;
import BabAl.BabalServer.domain.User;
import BabAl.BabalServer.dto.request.AddMealDto;
import BabAl.BabalServer.jwt.JwtUtil;
import BabAl.BabalServer.repository.UserRepository;
import BabAl.BabalServer.service.MainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/photo")
@Tag(name = "Photo Page", description = "Photo API")
public class PhotoController {
    private final MainService mainService;
    private final UserRepository userRepository;

    // raspberry pi camera module 사진 인식 결과 저장
    @PostMapping("/history") // 식사 기록 등록
    @Operation(summary = "raspberry pi camera 식사 등록", description = "식사 등록할 때 사용하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청이 정상 처리되었습니다", content = @Content(mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다", content = @Content(mediaType = "application/json"))
    })
    public ApiResponse<SuccessStatus> addHistory(@RequestHeader("X-MAC-ADDRESS") String macAddress,
            @Valid @RequestBody AddMealDto dto) {
        // macAddress 로 유저 조회하기
        User user = userRepository.findByCameraAddress(macAddress).orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));;
        String userEmail = user.getEmail();
        return ApiResponse.onSuccess(mainService.addHistory(userEmail, dto));
    }
}
