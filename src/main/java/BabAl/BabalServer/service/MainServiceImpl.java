package BabAl.BabalServer.service;

import BabAl.BabalServer.apiPayload.code.status.ErrorStatus;
import BabAl.BabalServer.apiPayload.code.status.SuccessStatus;
import BabAl.BabalServer.apiPayload.exception.GeneralException;
import BabAl.BabalServer.domain.Food;
import BabAl.BabalServer.domain.User;
import BabAl.BabalServer.dto.request.AddMealDto;
import BabAl.BabalServer.dto.response.MainHistoryResponseDto;
import BabAl.BabalServer.dto.response.MainStatisticsResponseDto;
import BabAl.BabalServer.repository.FoodRepository;
import BabAl.BabalServer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

    private final UserRepository userRepository;
    private final FoodRepository foodRepository;

    @Override
    public MainHistoryResponseDto getHistory(String userEmail, String date) {
        // 사용자 존재 여부 확인
        User user = checkUser(userEmail);

        // 날짜 변환
        LocalDate localDate = LocalDate.parse(date);

        // 사용자 - 날짜에 해당하는 음식 기록 조회
        List<Food> foodList = foodRepository.findAllByUserAndCreatedAt(user, localDate);

        return MainHistoryResponseDto.mainHistoryResponse(user, foodList);
    }

    @Override
    public SuccessStatus addHistory(String userEmail, AddMealDto dto) {
        return null;
    }

    @Override
    public MainStatisticsResponseDto getStatistics(String userEmail) {
        return null;
    }

    public User checkUser(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
