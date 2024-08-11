package BabAl.BabalServer.controller;

import BabAl.BabalServer.apiPayload.ApiResponse;
import BabAl.BabalServer.apiPayload.code.status.SuccessStatus;
import BabAl.BabalServer.dto.request.AddMealDto;
import BabAl.BabalServer.dto.response.MainHistoryResponseDto;
import BabAl.BabalServer.dto.response.MainStatisticsResponseDto;
import BabAl.BabalServer.jwt.JwtUtil;
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
@RequestMapping("/main")
@Tag(name = "Main Page", description = "Main Page API")
public class MainController {

    private final MainService mainService;

    @GetMapping("/history") // 식사 기록 조회
    @Operation(summary = "메인페이지-기록 조회", description = "식사 기록 조회할 때 사용하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다", content = @Content(mediaType = "application/json"))
    })
    public ApiResponse<MainHistoryResponseDto> getHistory(@RequestHeader("Authorization") String token,
                                                          @RequestParam("date") String date) {
        return ApiResponse.onSuccess(mainService.getHistory(extractUserEmail(token), date));
    }

    @PostMapping("/history") // 식사 기록 등록
    @Operation(summary = "메인페이지-기록 식사 등록", description = "식사 등록할 때 사용하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다", content = @Content(mediaType = "application/json"))
    })
    public ApiResponse<SuccessStatus> addHistory(@RequestHeader("Authorization") String token,
                                                 @Valid @RequestBody AddMealDto dto) {
        return ApiResponse.onSuccess(mainService.addHistory(extractUserEmail(token), dto));
    }

    @GetMapping("/statistics") // 통계 조회
    @Operation(summary = "메인페이지-통계 조회", description = "통계 조회할 때 사용하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다", content = @Content(mediaType = "application/json"))
    })
    public ApiResponse<MainStatisticsResponseDto> getStatistics(@RequestHeader("Authorization") String token,
                                                                @RequestParam("startDate") String startDate,
                                                                @RequestParam("endDate") String endDate) {
        return ApiResponse.onSuccess(mainService.getStatistics(extractUserEmail(token), startDate, endDate));
    }

    public String extractUserEmail(String token) {
        String jwtToken = token.substring(7);
        String userEmail = JwtUtil.getEmail(jwtToken);
        return userEmail;
    }
}
