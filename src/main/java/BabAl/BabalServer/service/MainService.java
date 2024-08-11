package BabAl.BabalServer.service;

import BabAl.BabalServer.apiPayload.code.status.SuccessStatus;
import BabAl.BabalServer.dto.request.AddMealDto;
import BabAl.BabalServer.dto.response.MainHistoryResponseDto;
import BabAl.BabalServer.dto.response.MainStatisticsResponseDto;
import BabAl.BabalServer.dto.response.MainStatisticsResponseListDto;

public interface MainService {

    // 식사 기록 조회
    MainHistoryResponseDto getHistory(String userEmail, String date);

    // 식사 기록 등록
    SuccessStatus addHistory(String userEmail, AddMealDto dto);

    // 식사 통계 조회
    MainStatisticsResponseListDto getStatistics(String userEmail, String startDate, String endDate);
}
